-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th6 06, 2024 lúc 06:35 PM
-- Phiên bản máy phục vụ: 10.4.28-MariaDB
-- Phiên bản PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `nh_chat_app`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `client`
--

CREATE TABLE `client` (
  `ClientID` varchar(50) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `BirthDate` date NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Password` varchar(200) NOT NULL,
  `Image` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `conversation__group`
--

CREATE TABLE `conversation__group` (
  `ConversationID` int(11) NOT NULL,
  `GroupName` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `conversation__group_members`
--

CREATE TABLE `conversation__group_members` (
  `ConversationID` int(11) NOT NULL,
  `ClientID` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `conversation__single`
--

CREATE TABLE `conversation__single` (
  `ConversationID` int(11) NOT NULL,
  `ClientID1` varchar(50) NOT NULL,
  `ClientID2` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `message_group`
--

CREATE TABLE `message_group` (
  `MessageID` int(11) NOT NULL,
  `ConversationID` int(11) NOT NULL,
  `SenderID` varchar(50) NOT NULL,
  `Content` text NOT NULL,
  `TimeSend` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `message_single`
--

CREATE TABLE `message_single` (
  `MessageID` int(11) NOT NULL,
  `ConversationID` int(11) NOT NULL,
  `SenderID` varchar(50) NOT NULL,
  `Content` text NOT NULL,
  `TimeSend` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`ClientID`);

--
-- Chỉ mục cho bảng `conversation__group`
--
ALTER TABLE `conversation__group`
  ADD PRIMARY KEY (`ConversationID`);

--
-- Chỉ mục cho bảng `conversation__group_members`
--
ALTER TABLE `conversation__group_members`
  ADD PRIMARY KEY (`ConversationID`,`ClientID`),
  ADD KEY `ClientID` (`ClientID`);

--
-- Chỉ mục cho bảng `conversation__single`
--
ALTER TABLE `conversation__single`
  ADD PRIMARY KEY (`ConversationID`),
  ADD KEY `ClientID1` (`ClientID1`),
  ADD KEY `ClientID2` (`ClientID2`);

--
-- Chỉ mục cho bảng `message_group`
--
ALTER TABLE `message_group`
  ADD PRIMARY KEY (`MessageID`),
  ADD KEY `ConversationID` (`ConversationID`),
  ADD KEY `SenderID` (`SenderID`);

--
-- Chỉ mục cho bảng `message_single`
--
ALTER TABLE `message_single`
  ADD PRIMARY KEY (`MessageID`),
  ADD KEY `ConversationID` (`ConversationID`),
  ADD KEY `SenderID` (`SenderID`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `conversation__group`
--
ALTER TABLE `conversation__group`
  MODIFY `ConversationID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `conversation__single`
--
ALTER TABLE `conversation__single`
  MODIFY `ConversationID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `message_group`
--
ALTER TABLE `message_group`
  MODIFY `MessageID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `message_single`
--
ALTER TABLE `message_single`
  MODIFY `MessageID` int(11) NOT NULL AUTO_INCREMENT;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `conversation__group_members`
--
ALTER TABLE `conversation__group_members`
  ADD CONSTRAINT `conversation__group_members_ibfk_1` FOREIGN KEY (`ConversationID`) REFERENCES `conversation__group` (`ConversationID`),
  ADD CONSTRAINT `conversation__group_members_ibfk_2` FOREIGN KEY (`ClientID`) REFERENCES `client` (`ClientID`);

--
-- Các ràng buộc cho bảng `conversation__single`
--
ALTER TABLE `conversation__single`
  ADD CONSTRAINT `conversation__single_ibfk_1` FOREIGN KEY (`ClientID1`) REFERENCES `client` (`ClientID`),
  ADD CONSTRAINT `conversation__single_ibfk_2` FOREIGN KEY (`ClientID2`) REFERENCES `client` (`ClientID`);

--
-- Các ràng buộc cho bảng `message_group`
--
ALTER TABLE `message_group`
  ADD CONSTRAINT `message_group_ibfk_1` FOREIGN KEY (`ConversationID`) REFERENCES `conversation__group` (`ConversationID`),
  ADD CONSTRAINT `message_group_ibfk_2` FOREIGN KEY (`SenderID`) REFERENCES `client` (`ClientID`);

--
-- Các ràng buộc cho bảng `message_single`
--
ALTER TABLE `message_single`
  ADD CONSTRAINT `message_single_ibfk_1` FOREIGN KEY (`ConversationID`) REFERENCES `conversation__single` (`ConversationID`),
  ADD CONSTRAINT `message_single_ibfk_2` FOREIGN KEY (`SenderID`) REFERENCES `client` (`ClientID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
