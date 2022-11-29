-- MySQL dump 10.13  Distrib 8.0.31, for Linux (x86_64)
--
-- Host: localhost    Database: paymybuddy
-- ------------------------------------------------------
-- Server version	8.0.31-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `paymybuddy`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `paymybuddy` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `paymybuddy`;

--
-- Table structure for table `bank_account`
--

DROP TABLE IF EXISTS `bank_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bank_account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `iban` varchar(27) NOT NULL,
  `date_of_creation` timestamp NOT NULL,
  `last_transaction_date` timestamp NULL DEFAULT NULL,
  `bic` varchar(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `deactivated` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `bank_account_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank_account`
--

LOCK TABLES `bank_account` WRITE;
/*!40000 ALTER TABLE `bank_account` DISABLE KEYS */;
INSERT INTO `bank_account` VALUES (1,1,'12432436567678','2022-11-05 10:40:19','2022-11-20 08:49:33','34567888','mon compte mis à jour',0),(2,1,'1432363758457','2022-11-05 10:40:25',NULL,'43677690456','nouveau compte',0),(3,1,'12432436567677','2022-11-06 14:13:54',NULL,'34567888','copy',0),(4,22,'2343654758653547658','2022-11-24 16:10:50','2022-11-24 16:28:18','34567845765','mon compte',1),(5,23,'1324635475869678','2022-11-27 15:34:39',NULL,'132463567','mon compte',0),(6,23,'1324635475869678','2022-11-26 17:18:19','2022-11-27 10:02:55','675890965','mon compte 2',0),(7,23,'2343654788465','2022-11-26 17:18:32','2022-11-27 10:03:22','32453667','mon compte 3',1),(8,23,'3265478697678','2022-11-26 17:18:46','2022-11-27 10:03:32','35467567876','mon compte 4',1),(9,23,'42343564769889','2022-11-26 17:18:59','2022-11-27 10:03:42','23456678','mon compte 5',0),(10,23,'1235436475866475','2022-11-28 16:47:56',NULL,'435647587','mon compte 6 ',1),(11,23,'46534758695467','2022-11-28 16:54:21','2022-11-28 17:14:42','345676589','mon nouveau compte updated',0),(12,23,'12354364758664755','2022-11-28 17:14:06','2022-11-28 17:16:34','435647587','compte',0);
/*!40000 ALTER TABLE `bank_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `external_transaction`
--

DROP TABLE IF EXISTS `external_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `external_transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,2) NOT NULL,
  `description` varchar(64) DEFAULT NULL,
  `timestamp` timestamp NOT NULL,
  `type` enum('CREDIT_EXTERNAL_ACCOUNT','DEBIT_EXTERNAL_ACCOUNT') NOT NULL,
  `bank_account_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `bank_account_id` (`bank_account_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `external_transaction_ibfk_1` FOREIGN KEY (`bank_account_id`) REFERENCES `bank_account` (`id`) ON DELETE CASCADE,
  CONSTRAINT `external_transaction_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `external_transaction`
--

LOCK TABLES `external_transaction` WRITE;
/*!40000 ALTER TABLE `external_transaction` DISABLE KEYS */;
INSERT INTO `external_transaction` VALUES (1,4000.00,'','2022-10-30 12:39:10','DEBIT_EXTERNAL_ACCOUNT',1,1),(2,1000.00,'','2022-10-30 12:40:16','CREDIT_EXTERNAL_ACCOUNT',1,1),(3,1000.00,'','2022-10-30 12:40:46','CREDIT_EXTERNAL_ACCOUNT',1,1),(4,100.00,'','2022-10-30 12:42:20','CREDIT_EXTERNAL_ACCOUNT',1,1),(5,999.95,'','2022-11-20 08:49:33','DEBIT_EXTERNAL_ACCOUNT',1,1),(6,50.00,'','2022-11-24 16:28:18','DEBIT_EXTERNAL_ACCOUNT',4,22),(7,10.00,'virement','2022-11-27 10:02:55','DEBIT_EXTERNAL_ACCOUNT',6,23),(8,12.00,'','2022-11-27 10:03:11','DEBIT_EXTERNAL_ACCOUNT',5,23),(9,10.00,'','2022-11-27 10:03:22','DEBIT_EXTERNAL_ACCOUNT',7,23),(10,10.00,'','2022-11-27 10:03:32','CREDIT_EXTERNAL_ACCOUNT',8,23),(11,8.00,'','2022-11-27 10:03:42','DEBIT_EXTERNAL_ACCOUNT',9,23),(12,10.00,'','2022-11-27 10:03:50','DEBIT_EXTERNAL_ACCOUNT',10,23),(13,10.00,'','2022-11-28 17:14:42','DEBIT_EXTERNAL_ACCOUNT',11,23),(14,1.00,'','2022-11-28 17:16:34','CREDIT_EXTERNAL_ACCOUNT',12,23);
/*!40000 ALTER TABLE `external_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payer_recipient`
--

DROP TABLE IF EXISTS `payer_recipient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payer_recipient` (
  `date_of_creation` timestamp NOT NULL,
  `last_transaction_date` timestamp NULL DEFAULT NULL,
  `payer_id` bigint NOT NULL,
  `recipient_id` bigint NOT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`payer_id`,`recipient_id`),
  KEY `recipient_id` (`recipient_id`),
  CONSTRAINT `payer_recipient_ibfk_1` FOREIGN KEY (`payer_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `payer_recipient_ibfk_2` FOREIGN KEY (`recipient_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payer_recipient`
--

LOCK TABLES `payer_recipient` WRITE;
/*!40000 ALTER TABLE `payer_recipient` DISABLE KEYS */;
INSERT INTO `payer_recipient` VALUES ('2022-11-13 13:25:36',NULL,1,3,0),('2022-11-20 08:51:46',NULL,3,1,0),('2022-11-24 16:25:06','2022-11-27 11:57:11',22,18,0),('2022-11-24 16:25:13',NULL,22,19,0),('2022-11-28 17:04:49','2022-11-28 17:17:28',23,9,0),('2022-11-28 17:18:19',NULL,23,11,0),('2022-11-28 17:03:51',NULL,23,12,0),('2022-11-26 20:28:29','2022-11-27 11:54:43',23,13,0),('2022-11-28 16:57:56',NULL,23,14,0),('2022-11-26 20:28:09','2022-11-27 11:56:01',23,17,0),('2022-11-26 20:27:31','2022-11-27 11:56:06',23,18,0),('2022-11-28 17:09:17',NULL,23,19,1),('2022-11-26 20:27:44',NULL,23,21,1),('2022-11-25 10:36:58','2022-11-27 11:55:20',23,22,1);
/*!40000 ALTER TABLE `payer_recipient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persistent_logins`
--

DROP TABLE IF EXISTS `persistent_logins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persistent_logins`
--

LOCK TABLES `persistent_logins` WRITE;
/*!40000 ALTER TABLE `persistent_logins` DISABLE KEYS */;
/*!40000 ALTER TABLE `persistent_logins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,2) NOT NULL,
  `commission` decimal(10,2) NOT NULL,
  `description` varchar(64) DEFAULT NULL,
  `timestamp` timestamp NOT NULL,
  `payer_id` bigint NOT NULL,
  `recipient_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `payer_id` (`payer_id`,`recipient_id`),
  CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`payer_id`, `recipient_id`) REFERENCES `payer_recipient` (`payer_id`, `recipient_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
INSERT INTO `transaction` VALUES (1,2.00,0.01,'','2022-11-13 13:25:49',1,3),(2,1.00,0.01,'','2022-11-27 11:54:43',23,13),(3,1.00,0.01,'virement marion','2022-11-27 11:54:56',23,17),(4,2.00,0.01,'virement luc','2022-11-27 11:55:20',23,22),(5,2.00,0.01,'','2022-11-27 11:55:54',23,19),(6,2.00,0.01,'','2022-11-27 11:56:01',23,17),(7,1.00,0.01,'','2022-11-27 11:56:06',23,18),(8,1.00,0.01,'','2022-11-27 11:57:10',22,18),(9,2.00,0.01,'','2022-11-28 17:17:28',23,9);
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(64) NOT NULL,
  `date_of_creation` timestamp NOT NULL,
  `last_online_time` timestamp NULL DEFAULT NULL,
  `balance` decimal(10,2) NOT NULL,
  `first_name` varchar(64) NOT NULL,
  `last_name` varchar(64) NOT NULL,
  `address` varchar(255) NOT NULL,
  `phone` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'toto@de.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-06-22 16:01:44','2022-11-13 13:50:51',999.95,'toto','toto','adresse de toto','0612345678'),(2,'hortense@de.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-06-22 16:01:44','2022-06-22 16:01:44',10.00,'hortense','hortense','adresse de hortense','0612345678'),(3,'frederique@de.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-06-22 16:01:44','2022-06-22 16:01:44',12.00,'frédérique','frédérique','adresse de frédérique','0612345678'),(4,'julien.bouchard@gmail.com','12341234','2022-10-30 10:28:35',NULL,0.00,'Julien','Bouchard','adresse de julien','0612345678'),(5,'julien.bouchard2@gmail.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-06 14:23:06',NULL,0.00,'Julien','Bouchard','adresse de julien','0612345678'),(6,'jd@abc.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-13 09:21:27',NULL,0.00,'John','Doe','adresse','0612345678'),(7,'jd2@abc.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-13 09:25:38',NULL,0.00,'John','Doe','adresse','0612345678'),(8,'jd3@abc.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-13 09:40:22',NULL,0.00,'John','Doe','adresse','0612345678'),(9,'albert.alfonsi@gmail.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-18 12:05:14',NULL,2.00,'Albert','Alfonsi','adresse d\'albert','0612345678'),(10,'albert.alfonsi2@gmail.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-18 12:06:27',NULL,0.00,'Albert','Alfonsi','adresse d\'albert','0612345678'),(11,'eve.lalu@gmail.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-20 09:03:38',NULL,0.00,'Eve','Lalu','adresse d\'eve','0612345678'),(12,'barb@gmail.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-20 09:06:38',NULL,0.00,'Barbara','Guicheteau','adresse de barbara','0612345678'),(13,'eve.guicheteau@gmail.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-20 14:34:47',NULL,1.00,'Eve','Guicheteau','adresse d\'eve','0612345678'),(14,'marie.martin@gmail.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-20 17:50:11',NULL,0.00,'Marie','Martin','adresse de Marie','0612345678'),(15,'mairie.martin2@gmail.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-20 17:51:33',NULL,0.00,'Marie','Martin','adresse de Marie','0612345678'),(16,'anthony@gmail.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-24 09:54:31',NULL,0.00,'Anthony','Herbert','adresse d\'anthony','0612345678'),(17,'marion@gmail.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-24 09:56:04',NULL,3.00,'Marion','Daguerre','adresse de marion','0612345678'),(18,'paul@gmail.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-24 09:58:53','2022-11-27 08:19:45',2.00,'Paul','Bordes','adresse de paul','0612345678'),(19,'paulb@gmail.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-24 10:05:42','2022-11-27 08:19:55',2.00,'Paul','Bordes','adresse de paul','0612345678'),(20,'paulbo@gmail.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-24 10:08:12',NULL,0.00,'Paul','Bordes','adresse de paul','0612345678'),(21,'paulbordes@gmail.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-24 10:10:16',NULL,0.00,'Paul','Bordes','adresse de paul','0612345678'),(22,'luc@gmail.com','$2a$10$yiURkrp/M37q5zPo9.e7bOe0nBnJ0S8XnIVf/GwN73Uzl0IpRYDnG','2022-11-24 11:23:20','2022-11-27 11:56:24',51.00,'Luc','François','adresse de luc','0612345678'),(23,'anne@gmail.com','$2a$10$0Q5J4SO2m3UcCCkGZArb/OTL6HqqluvofAJL/xBlP0AG816/sriw2','2022-11-25 10:23:37','2022-11-28 17:28:03',37.96,'Anne','Rivière','adresse d\'anne','0612345678'),(24,'paul.b@gmail.com','$2a$10$hQL9lKjmz02c0tQ1eIVAPON8x7AKQ7gVa2rySu8/KeEyKLLyNQ4Du','2022-11-27 08:20:56','2022-11-27 08:21:05',0.00,'Paul','Bordes','adresse de paul','0612345678');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-11-29 21:36:37
