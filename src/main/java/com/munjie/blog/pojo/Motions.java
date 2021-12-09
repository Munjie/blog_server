package com.munjie.blog.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Motions {

    private List<Idle> idle;
    private List<Flick_head> flick_head;
    private List<Tap_body> tap_body;


}