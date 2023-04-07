package com.yuripe.normalizator.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "code")})
public class SFTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sftpId;

    @NotBlank
    @Size(max = 12)
    private Long code;

    @NotBlank
    @Size(max = 50)
    private String host;

    @NotNull
    private int port;

    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(max = 70)
    private String password;

    @NotBlank
    @Size(max = 10)
    private String authType;

    public SFTP(Long sftpId, Long code, String host, int port, String username, String password, String authType) {
        this.sftpId = sftpId;
        this.code = code;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.authType = authType;
    }

    public Long getSftpId() {
        return sftpId;
    }

    public void setSftpId(Long sftpId) {
        this.sftpId = sftpId;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }
}
