package com.fftl.springfftltodo.service;

import com.fftl.springfftltodo.dto.EmailValidResponse;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class EmailValidService {

    // 1) 디스포저블 도메인 목록 (classpath:/disposable-domains.txt 에서 로드)
    private final Set<String> disposableDomains = new HashSet<>();

    // 2) 오타 제안용 자주 쓰는 도메인
    private static final List<String> COMMON_DOMAINS = List.of(
            "gmail.com", "naver.com", "daum.net", "nate.com",
            "outlook.com", "hotmail.com", "icloud.com", "hanmail.net", "kakao.com", "yahoo.com"
    );

    public EmailValidService() {
        loadDisposableList();
    }

    public EmailValidResponse validate(String rawEmail) {
        String email = (rawEmail == null) ? "" : rawEmail.trim();

        boolean formatValid = isValidFormat(email);
        if (!formatValid) {
            return new EmailValidResponse(false, false, false, false, null);
        }

        String domain = extractDomain(email);
        boolean domainValid = (domain != null && domain.contains("."));
        if (!domainValid) {
            return new EmailValidResponse(true, false, false, false, null);
        }

        boolean mxFound = hasMxRecord(domain);
        boolean disposable = isDisposable(domain);
        String suggestion = suggestDomain(domain);

        return new EmailValidResponse(true, true, mxFound, disposable, suggestion);
    }

    /* ---------- 구현 상세 ---------- */

    private boolean isValidFormat(String email) {
        try {
            // RFC 수준의 엄격한 형식 검증
            InternetAddress addr = new InternetAddress(email, true);
            addr.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }

    private String extractDomain(String email) {
        int at = email.lastIndexOf('@');
        if (at < 0 || at == email.length() - 1) return null;
        return email.substring(at + 1).toLowerCase(Locale.ROOT);
    }

    /** JNDI를 이용한 MX 레코드 조회 (추가 라이브러리 불필요) */
    private boolean hasMxRecord(String domain) {
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            DirContext ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(domain, new String[]{"MX"});
            Attribute mx = attrs.get("MX");
            if (mx != null && mx.size() > 0) return true;

            // MX가 없으면 A 레코드로도 수신 가능한 경우가 있어 A 조회 fallback
            attrs = ctx.getAttributes(domain, new String[]{"A"});
            Attribute a = attrs.get("A");
            return a != null && a.size() > 0;
        } catch (NamingException e) {
            return false;
        }
    }

    private boolean isDisposable(String domain) {
        return disposableDomains.contains(domain);
    }

    /** 간단한 도메인 오타 제안 (레벤슈타인 거리 <= 2) */
    private String suggestDomain(String domain) {
        String best = null;
        int bestDist = Integer.MAX_VALUE;
        for (String cand : COMMON_DOMAINS) {
            int d = levenshtein(domain, cand);
            if (d < bestDist) {
                bestDist = d;
                best = cand;
            }
        }
        return (bestDist <= 2 && !domain.equals(best)) ? best : null;
    }

    private int levenshtein(String a, String b) {
        int n = a.length(), m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }
        return dp[n][m];
    }

    private void loadDisposableList() {
        try {
            ClassPathResource res = new ClassPathResource("disposable-domains.txt");
            if (!res.exists()) {
                // 최소 샘플 (운영에서는 파일을 꼭 두길 권장)
                disposableDomains.addAll(List.of("mailinator.com", "tempmail.com", "10minutemail.com"));
                return;
            }
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim().toLowerCase(Locale.ROOT);
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    disposableDomains.add(line);
                }
            }
        } catch (Exception e) {
            // 실패해도 서비스는 동작하도록: 기본 샘플만 유지
            disposableDomains.addAll(List.of("mailinator.com", "tempmail.com", "10minutemail.com"));
        }
    }
}
