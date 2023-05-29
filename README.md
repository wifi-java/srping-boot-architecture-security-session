srping-boot-architecture-security-session  

srpingboot 3.0  
java 17  
mysql 8.0  
  
테스트를 위한 테이블 생성 필요.  
  
CREATE TABLE `tb_member` (  
  `IDX` BIGINT(19) NOT NULL AUTO_INCREMENT,  
  `ID` VARCHAR(100) NULL DEFAULT NULL COMMENT '아이디' COLLATE 'utf8mb4_0900_ai_ci',  
  `NAME` VARCHAR(50) NOT NULL COMMENT '이름' COLLATE 'utf8mb4_0900_ai_ci',  
  `PW` VARCHAR(200) NOT NULL COMMENT '비밀번호' COLLATE 'utf8mb4_0900_ai_ci',  
  `CHANNEL` CHAR(1) NULL DEFAULT NULL COMMENT '가입 채널' COLLATE 'utf8mb4_0900_ai_ci',  
  PRIMARY KEY (`IDX`) USING BTREE  
)  
COLLATE='utf8mb4_0900_ai_ci'  
ENGINE=InnoDB  
AUTO_INCREMENT=128;  
