package com.lcwd.electronic.store.helper;

import com.lcwd.electronic.store.dtos.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ToPageableResponse {


    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page,Class<V> type){
        List<U> list=page.getContent();
        List<V> content=list.stream().map(obj-> new ModelMapper().map(obj,type)).toList();

        PageableResponse<V> resp=new PageableResponse<>();
        resp.setContent(content);
        resp.setPageNumber(page.getNumber());
        resp.setPageSize(page.getSize());
        resp.setTotalPage(page.getTotalPages());
        resp.setTotalElements(page.getTotalElements());
        resp.setLastPage(page.isLast());
        return resp;
    }


}
