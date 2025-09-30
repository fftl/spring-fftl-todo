package com.fftl.springfftltodo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int routineId;

    @OneToMany(mappedBy = "routine")
    private List<Todo> todoList;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
