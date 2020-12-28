package com.example.four.Bean;

public class AddressDto {
    int addrNo;
    String addrName;
    String addrTel;
    String addrAddr;
    String addrDetail; // 상세
    String addrLike; //즐겨찾기 1,0  0이 안눌렸을때
    String addrTag;
    String addrImagePath;

    public AddressDto(int addrNo, String addrName, String addrTel, String addrAddr, String addrDetail, String addrTag) {
        this.addrNo = addrNo;
        this.addrName = addrName;
        this.addrTel = addrTel;
        this.addrAddr = addrAddr;
        this.addrDetail = addrDetail;
        this.addrTag = addrTag;
    }

    public AddressDto(int addrNo, String addrName, String addrTel, String addrAddr, String addrDetail, String addrTag, String addrImagePath) {
        this.addrNo = addrNo;
        this.addrName = addrName;
        this.addrTel = addrTel;
        this.addrAddr = addrAddr;
        this.addrDetail = addrDetail;
        this.addrTag = addrTag;
        this.addrImagePath = addrImagePath;
    }

    public AddressDto(int addrNo, String addrName, String addrTel, String addrAddr, String addrDetail, String addrLike, String addrTag, String addrImagePath) {
        this.addrNo = addrNo;
        this.addrName = addrName;
        this.addrTel = addrTel;
        this.addrAddr = addrAddr;
        this.addrDetail = addrDetail;
        this.addrLike = addrLike;
        this.addrTag = addrTag;
        this.addrImagePath = addrImagePath;
    }

    public int getAddrNo() {
        return addrNo;
    }

    public void setAddrNo(int addrNo) {
        this.addrNo = addrNo;
    }

    public String getAddrName() {
        return addrName;
    }

    public void setAddrName(String addrName) {
        this.addrName = addrName;
    }

    public String getAddrTel() {
        return addrTel;
    }

    public void setAddrTel(String addrTel) {
        this.addrTel = addrTel;
    }

    public String getAddrAddr() {
        return addrAddr;
    }

    public void setAddrAddr(String addrAddr) {
        this.addrAddr = addrAddr;
    }

    public String getAddrDetail() {
        return addrDetail;
    }

    public void setAddrDetail(String addrDetail) {
        this.addrDetail = addrDetail;
    }

    public String getAddrLike() {
        return addrLike;
    }

    public void setAddrLike(String addrLike) {
        this.addrLike = addrLike;
    }

    public String getAddrTag() {
        return addrTag;
    }

    public void setAddrTag(String addrTag) {
        this.addrTag = addrTag;
    }

    public String getAddrImagePath() {
        return addrImagePath;
    }

    public void setAddrImagePath(String addrImagePath) {
        this.addrImagePath = addrImagePath;
    }
}
