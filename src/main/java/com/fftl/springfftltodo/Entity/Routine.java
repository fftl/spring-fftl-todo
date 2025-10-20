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

    private String routineName;
    private List<String> todoList;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Routine(String routineName, List<String> todoList, Member member){
        this.routineName = routineName;
        this.todoList = todoList;
        this.member = member;
    }
}
