-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.4.8-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- music 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `music` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `music`;

-- 테이블 music.관리영역 구조 내보내기
CREATE TABLE IF NOT EXISTS `관리영역` (
  `부서_식별번호` varchar(50) NOT NULL,
  `관리영역` varchar(50) NOT NULL,
  PRIMARY KEY (`관리영역`,`부서_식별번호`),
  KEY `부서_식별번호` (`부서_식별번호`),
  CONSTRAINT `FK_관리영역_부서` FOREIGN KEY (`부서_식별번호`) REFERENCES `부서` (`식별번호`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 music.관리영역:~7 rows (대략적) 내보내기
/*!40000 ALTER TABLE `관리영역` DISABLE KEYS */;
INSERT IGNORE INTO `관리영역` (`부서_식별번호`, `관리영역`) VALUES
	('4', 'A/S'),
	('4', '고객응대'),
	('3', '관리'),
	('3', '교육'),
	('2', '데이터관리'),
	('2', '데이터유지보수'),
	('1', '채용');
/*!40000 ALTER TABLE `관리영역` ENABLE KEYS */;

-- 테이블 music.관리자 구조 내보내기
CREATE TABLE IF NOT EXISTS `관리자` (
  `건물번호` varchar(50) DEFAULT NULL,
  `도로명` varchar(50) DEFAULT NULL,
  `식별_ID` varchar(50) NOT NULL DEFAULT '',
  `주민등록번호` varchar(50) DEFAULT NULL,
  `이름` varchar(20) DEFAULT NULL,
  `성별` char(50) DEFAULT NULL,
  `부서_식별번호` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`식별_ID`),
  KEY `부서_식별번호_등록_음악ID_삭제_음악ID` (`부서_식별번호`),
  CONSTRAINT `FK_관리자_부서` FOREIGN KEY (`부서_식별번호`) REFERENCES `부서` (`식별번호`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 music.관리자:~5 rows (대략적) 내보내기
/*!40000 ALTER TABLE `관리자` DISABLE KEYS */;
INSERT IGNORE INTO `관리자` (`건물번호`, `도로명`, `식별_ID`, `주민등록번호`, `이름`, `성별`, `부서_식별번호`) VALUES
	('30', '도선동', '1', '939884-3342332', '이영석', '남자', '1'),
	('21', '무학', '2', '929392-3232323', '서건식', '남자', '2'),
	('138', '사동로', '3', '938828-3323332', '조동훈', '남자', '3'),
	('443', '이천로', '4', '685343-1324242', '이미자', '여자', '4'),
	('32', '왕십리로 3길', '5', '988838-1132332', '이건희', '남자', '4');
/*!40000 ALTER TABLE `관리자` ENABLE KEYS */;

-- 테이블 music.부서 구조 내보내기
CREATE TABLE IF NOT EXISTS `부서` (
  `식별번호` varchar(50) NOT NULL DEFAULT '',
  `부서이름` varchar(50) DEFAULT NULL,
  `부서_관리자ID` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`식별번호`),
  KEY `부서_관리자ID` (`부서_관리자ID`),
  CONSTRAINT `FK_부서_관리자` FOREIGN KEY (`부서_관리자ID`) REFERENCES `관리자` (`식별_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 music.부서:~4 rows (대략적) 내보내기
/*!40000 ALTER TABLE `부서` DISABLE KEYS */;
INSERT IGNORE INTO `부서` (`식별번호`, `부서이름`, `부서_관리자ID`) VALUES
	('1', '인사', '1'),
	('2', '백엔드', '2'),
	('3', '정훈', '3'),
	('4', '고객관리', '4');
/*!40000 ALTER TABLE `부서` ENABLE KEYS */;

-- 테이블 music.부서위치 구조 내보내기
CREATE TABLE IF NOT EXISTS `부서위치` (
  `부서_식별번호` varchar(50) NOT NULL,
  `부서위치` varchar(50) NOT NULL,
  PRIMARY KEY (`부서위치`,`부서_식별번호`),
  KEY `부서_식별번호` (`부서_식별번호`),
  CONSTRAINT `FK_부서위치_부서` FOREIGN KEY (`부서_식별번호`) REFERENCES `부서` (`식별번호`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 music.부서위치:~7 rows (대략적) 내보내기
/*!40000 ALTER TABLE `부서위치` DISABLE KEYS */;
INSERT IGNORE INTO `부서위치` (`부서_식별번호`, `부서위치`) VALUES
	('4', '세종'),
	('1', '왕십리'),
	('3', '왕십리'),
	('2', '이천'),
	('3', '이천'),
	('2', '판교'),
	('4', '판교');
/*!40000 ALTER TABLE `부서위치` ENABLE KEYS */;

-- 테이블 music.사용자 구조 내보내기
CREATE TABLE IF NOT EXISTS `사용자` (
  `ID` varchar(50) NOT NULL,
  `Password` varchar(50) DEFAULT NULL,
  `이름` varchar(50) DEFAULT NULL,
  `성별` char(50) DEFAULT NULL,
  `핸드폰번호` varchar(50) DEFAULT NULL,
  `공유받은ID` varchar(50) DEFAULT NULL,
  `소유한이용권` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `공유한ID_플레이리스트등록_ID_플레이리스트삭제_ID_소유한이용권` (`공유받은ID`,`소유한이용권`),
  KEY `FK_사용자_이용권` (`소유한이용권`),
  CONSTRAINT `FK_사용자_사용자` FOREIGN KEY (`공유받은ID`) REFERENCES `사용자` (`ID`),
  CONSTRAINT `FK_사용자_이용권` FOREIGN KEY (`소유한이용권`) REFERENCES `이용권` (`이용권이름`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 music.사용자:~5 rows (대략적) 내보내기
/*!40000 ALTER TABLE `사용자` DISABLE KEYS */;
INSERT IGNORE INTO `사용자` (`ID`, `Password`, `이름`, `성별`, `핸드폰번호`, `공유받은ID`, `소유한이용권`) VALUES
	('123', '123', '건식', '남자', '010-3232-3232', 'id', NULL),
	('f', 'f', 'f', '여자', '010-1242-3434', 'id', '정기권'),
	('id', 'pw', '이영석', '남자', '010-3241-1324', 'iqeq2328', '스트리밍'),
	('iqeq2328', '1', '건식이', '여자', '01023223143', 'id', '정기권'),
	('whehdgns', 'whehdgns', '조동훈', '남자', '01023020030', NULL, NULL);
/*!40000 ALTER TABLE `사용자` ENABLE KEYS */;

-- 테이블 music.음악 구조 내보내기
CREATE TABLE IF NOT EXISTS `음악` (
  `제목` varchar(50) DEFAULT NULL,
  `아티스트` varchar(50) DEFAULT NULL,
  `장르` varchar(50) DEFAULT NULL,
  `가사` varchar(10000) DEFAULT NULL,
  `플레이시간` varchar(50) DEFAULT NULL,
  `등록일자` date DEFAULT NULL,
  `발매일자` date DEFAULT NULL,
  `앨범제목` varchar(50) DEFAULT NULL,
  `뮤직비디오` varchar(100) DEFAULT NULL,
  `음악ID` int(11) NOT NULL DEFAULT 0,
  `순위` varchar(50) NOT NULL,
  PRIMARY KEY (`음악ID`,`순위`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 music.음악:~4 rows (대략적) 내보내기
/*!40000 ALTER TABLE `음악` DISABLE KEYS */;
INSERT IGNORE INTO `음악` (`제목`, `아티스트`, `장르`, `가사`, `플레이시간`, `등록일자`, `발매일자`, `앨범제목`, `뮤직비디오`, `음악ID`, `순위`) VALUES
	('포장마차', '황인욱', '발라드', '그대와 자주 가던 그 술집에 혼자 널 생각하며 소주 한잔해그대가 좋아하던 김치찌개를가만히 바라보다 눈물 한잔해그 사람 왔었나요아니 소식이라도그녀에게 전해줘요늘 지금처럼 기다린다고포장마차 그때 그 자리에서네가 있던 그곳에 서서날 사랑한다 말했잖아영원할 거라고 말했잖아 포장마차 그때 그 자리에서 돌아오라는 말을 다시', '4분10초', '2019-11-27', '2019-04-22', '포장마차', 'https://www.youtube.com/watch?v=Sfsf3WdJ8xo', 1, '1'),
	('이별행동', '이우', '발라드', '다시금 날 봐줘요', '3분22초', '2019-11-29', '2019-08-21', '이우', 'https://www.youtube.com/watch?v=D1PvIWdJ124', 2, '2'),
	('Blueming', 'IU', '락', 'Love~', '3분28초', '2019-12-02', '2019-11-30', 'Love Poem', 'https://www.youtube.com/watch?v=D1PvIWdJ8xo', 3, '3'),
	('늦은 밤 너의 집 앞 골목길에서', '노을', '발라드', '늦은밤~', '4분20초', '2019-12-02', '2019-11-07', '디지털싱글', 'https://www.youtube.com/watch?v=D1PvIWdJ8xo', 4, '4');
/*!40000 ALTER TABLE `음악` ENABLE KEYS */;

-- 테이블 music.이용권 구조 내보내기
CREATE TABLE IF NOT EXISTS `이용권` (
  `이용권이름` varchar(50) NOT NULL,
  `이벤트유무` varchar(50) DEFAULT NULL,
  `기간` varchar(50) DEFAULT NULL,
  `허용서비스` varchar(50) DEFAULT NULL,
  `가격` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`이용권이름`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 music.이용권:~2 rows (대략적) 내보내기
/*!40000 ALTER TABLE `이용권` DISABLE KEYS */;
INSERT IGNORE INTO `이용권` (`이용권이름`, `이벤트유무`, `기간`, `허용서비스`, `가격`) VALUES
	('스트리밍', '1', '1m', 'streaming', '30만원'),
	('정기권', '1', '3m', 'all', '20만원');
/*!40000 ALTER TABLE `이용권` ENABLE KEYS */;

-- 테이블 music.플레이리스트 구조 내보내기
CREATE TABLE IF NOT EXISTS `플레이리스트` (
  `소유한사용자` varchar(50) NOT NULL,
  `음악ID` int(11) NOT NULL DEFAULT 0,
  `제목` varchar(50) NOT NULL,
  PRIMARY KEY (`소유한사용자`,`음악ID`,`제목`),
  KEY `소유한사용자` (`소유한사용자`),
  KEY `FK_플레이리스트_음악` (`음악ID`),
  KEY `제목` (`제목`),
  CONSTRAINT `FK_플레이리스트_사용자` FOREIGN KEY (`소유한사용자`) REFERENCES `사용자` (`ID`),
  CONSTRAINT `FK_플레이리스트_음악` FOREIGN KEY (`음악ID`) REFERENCES `음악` (`음악ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 music.플레이리스트:~6 rows (대략적) 내보내기
/*!40000 ALTER TABLE `플레이리스트` DISABLE KEYS */;
INSERT IGNORE INTO `플레이리스트` (`소유한사용자`, `음악ID`, `제목`) VALUES
	('123', 1, 'df'),
	('f', 1, '발라드'),
	('id', 1, 'EDM'),
	('id', 2, '발라드'),
	('id', 3, '락'),
	('iqeq2328', 2, '락');
/*!40000 ALTER TABLE `플레이리스트` ENABLE KEYS */;

-- 테이블 music.핸드폰번호 구조 내보내기
CREATE TABLE IF NOT EXISTS `핸드폰번호` (
  `관리자_식별ID` varchar(50) NOT NULL,
  `핸드폰번호` varchar(20) NOT NULL DEFAULT '010-0000-0000',
  PRIMARY KEY (`핸드폰번호`,`관리자_식별ID`),
  KEY `관리자_식별ID` (`관리자_식별ID`),
  CONSTRAINT `FK_핸드폰번호_관리자` FOREIGN KEY (`관리자_식별ID`) REFERENCES `관리자` (`식별_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 music.핸드폰번호:~8 rows (대략적) 내보내기
/*!40000 ALTER TABLE `핸드폰번호` DISABLE KEYS */;
INSERT IGNORE INTO `핸드폰번호` (`관리자_식별ID`, `핸드폰번호`) VALUES
	('1', '010-2234-1422'),
	('2', '010-2512-3244'),
	('4', '010-3232-1111'),
	('5', '010-3232-1313'),
	('2', '010-3235-1423'),
	('3', '010-4232-1323'),
	('5', '010-4242-1313'),
	('1', '010-5522-1323');
/*!40000 ALTER TABLE `핸드폰번호` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
