package com.fftl.springfftltodo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "routines")
@Entity
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int routineId;

    private String routineName;

    @ElementCollection
    @CollectionTable(
            name = "routine_todo_list",
            joinColumns = @JoinColumn(name = "routine_id")
    )
    @Column(name = "todo_item")
    private List<String> todoList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Routine(String routineName, List<String> todoList, Member member){
        this.routineName = routineName;
        this.todoList = todoList;
        this.member = member;
    }
}
