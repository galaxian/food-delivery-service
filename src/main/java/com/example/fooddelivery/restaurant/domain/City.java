package com.example.fooddelivery.restaurant.domain;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum City {
	GANGNAM("강남구"),
	GANGDONG("강동구"),
	GANGBUK("강북구"),
	GANGSEO("강서구"),
	GWANAK("관악구"),
	GWANGJIN("광진구"),
	GURO("구로구"),
	GEUMCHEON("금천구"),
	NOWON("노원구"),
	DOBONG("도봉구"),
	DONGDAEMOON("동대문구"),
	DONGJAK("동작구"),
	MAPO("마포구"),
	SEODAEMOON("서대문구"),
	SEOCHO("서초구"),
	SUNGDONG("성동구"),
	SUNGBUK("성북구"),
	SONGPA("송파구"),
	YANGCHEON("양천구"),
	YEONGDEUNGPO("영등포구"),
	YONGSAN("용산구"),
	EUNPYEONG("은평구"),
	JONGNO("종로구"),
	JUNG("중구"),
	JUNGNANG("중랑구");

	private String city;

	City(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public static City getEnumCity(String city) {
		return Arrays.stream(City.values())
			.filter(c -> c.getCity().equals(city))
			.sequential()
			.collect(Collectors.toList())
			.get(0);
	}
}
