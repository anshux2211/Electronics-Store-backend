package com.example.electronic.store.helper;

import com.example.electronic.store.dtos.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;

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
