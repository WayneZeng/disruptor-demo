package com.example.netty.common.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: yanuo
 * @create: 20201228 8:51 PM
 */

@Data
public class ContentDTO implements Serializable {

    private String id;

    private String name;

    private String message;

}
