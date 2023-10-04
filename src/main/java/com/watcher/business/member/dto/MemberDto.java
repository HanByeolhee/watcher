package com.watcher.business.member.dto;


import com.watcher.business.comm.dto.CommDto;

public class MemberDto extends CommDto {

    private String id;              // key
    private String password;        // 패스워드
    private String loginId;         // 회원 아이디
    private String email;           // 이메일
    private String phoneNum;        // 회원 휴대푠 번호
    private String gender;          // 회원 성별
    private String birth;           // 회원 생년월일
    private String name;            // 회원 이름
    private String nickname;        // 회원 유형별명
    private String memType;         // 회원 유형
    private String memProfileImg;   // 회원 프로필
    private String level;           // 회원 등급

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMemType() {
        return memType;
    }

    public void setMemType(String memType) {
        this.memType = memType;
    }

    public String getMemProfileImg() {
        return memProfileImg;
    }

    public void setMemProfileImg(String memProfileImg) {
        this.memProfileImg = memProfileImg;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}