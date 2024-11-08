package com.example.incident.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Incident {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String description;
    private String status;
}