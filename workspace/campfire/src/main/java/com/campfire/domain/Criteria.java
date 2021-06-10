package com.campfire.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Criteria {
	private int pageNum;
	private int amount;
	
	private String from; // 기간
	private String type; // 분야 
	private String keyword;
	
	public Criteria() {
		this(1, 12);
	}
	
	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
	
	
	// 타입 배열로 만드는 메소드
	public String[] getTypeList() {
		return type == null ? new String[] {} : type.split("");
	}
	
	public String[] getFromList() {
		return from == null ? new String[] {} : from.split("");
	}
	
	
	
	public String getListLink() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
			.queryParam("pageNum", pageNum)
			.queryParam("amount", amount)
			.queryParam("from", from)
			.queryParam("type", type)
			.queryParam("keyword", keyword);
			
		return builder.toUriString();
	}
}
