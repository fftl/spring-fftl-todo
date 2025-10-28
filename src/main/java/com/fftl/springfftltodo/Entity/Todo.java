package com.fftl.springfftltodo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name="todos", indexes = {
        @Index(name="idx__member__date", columnList = "member_id, date")
})
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int todoId;

    private String text;
    private LocalDate date;
    private boolean isChecked;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Todo(String text, Member member){
        this.text = text;
        this.member = member;
        this.date = LocalDate.now();
    }

    public void checkTodo(){
        this.isChecked = !this.isChecked;
    }
}
