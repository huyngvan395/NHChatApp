-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th6 23, 2024 lúc 07:44 AM
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
  `Email` varchar(100) NOT NULL,
  `Password` varchar(200) NOT NULL,
  `Image` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `client`
--

INSERT INTO `client` (`ClientID`, `Name`, `Email`, `Password`, `Image`) VALUES
('ngt', 'Việt Nam', 'vietnam@gmail.com', '3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2', 'file:/D:/AdvancedJavaFinalTerm/NHChatApp/Chat_Client/client_storage/Image_Avatar/ngt_avatar.jpg'),
('nnn', 'Tôi là Huy', 'ngvanhuy@gmail.com', '3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2', 'file:/D:/AdvancedJavaFinalTerm/NHChatApp/Chat_Client/client_storage/Image_Avatar/nnn_avatar.png'),
('nvh1', 'Nguyễn Văn Huy', 'ngvanhuy0000@gmail.com', '3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2', 'file:/D:/AdvancedJavaFinalTerm/NHChatApp/Chat_Client/client_storage/Image_Avatar/nvh1_avatar.jpg'),
('vkum', 'VKU Member', 'member@gmail.com', '3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2', 'file:/D:/AdvancedJavaFinalTerm/NHChatApp/Chat_Client/client_storage/Image_Avatar/vkum_avatar.jpg');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `conversation_bot`
--

CREATE TABLE `conversation_bot` (
  `ConversationID` int(11) NOT NULL,
  `Chatbot` varchar(20) NOT NULL,
  `ClientID` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `conversation_bot`
--

INSERT INTO `conversation_bot` (`ConversationID`, `Chatbot`, `ClientID`) VALUES
(1, 'chatbot', 'nvh1'),
(2, 'chatbot', 'ngt');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `conversation_group`
--

CREATE TABLE `conversation_group` (
  `ConversationID` int(11) NOT NULL,
  `GroupName` varchar(100) NOT NULL,
  `Image_Group` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `conversation_group`
--

INSERT INTO `conversation_group` (`ConversationID`, `GroupName`, `Image_Group`) VALUES
(6, 'Group_Vip', 'file:/D:/AdvancedJavaFinalTerm/NHChatApp/Chat_Client/client_storage/Image_Group/8cd200fa145e00fcff947e8b6c5c15e0.jpg');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `conversation_group_members`
--

CREATE TABLE `conversation_group_members` (
  `ConversationID` int(11) NOT NULL,
  `ClientID` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `conversation_group_members`
--

INSERT INTO `conversation_group_members` (`ConversationID`, `ClientID`) VALUES
(6, 'ngt'),
(6, 'nnn'),
(6, 'nvh1');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `conversation_single`
--

CREATE TABLE `conversation_single` (
  `ConversationID` int(11) NOT NULL,
  `ClientID1` varchar(50) NOT NULL,
  `ClientID2` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `conversation_single`
--

INSERT INTO `conversation_single` (`ConversationID`, `ClientID1`, `ClientID2`) VALUES
(24, 'nvh1', 'nnn'),
(25, 'ngt', 'nvh1');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `message_bot`
--

CREATE TABLE `message_bot` (
  `MessageID` int(11) NOT NULL,
  `ConversationID` int(11) NOT NULL,
  `ID` varchar(50) NOT NULL,
  `Message` text NOT NULL,
  `MessageType` text NOT NULL,
  `TimeSend` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `message_bot`
--

INSERT INTO `message_bot` (`MessageID`, `ConversationID`, `ID`, `Message`, `MessageType`, `TimeSend`) VALUES
(1, 1, 'nvh1', 'Hello', 'Text', '2024-06-18 13:05:29'),
(2, 1, 'chatbot', 'hello! 👋 I\'m happy to hear from you. How can i help today? 😊', 'Text', '2024-06-18 13:05:33'),
(3, 1, 'nvh1', '1+1 bằng mấy ', 'Text', '2024-06-18 15:54:30'),
(4, 1, 'chatbot', '  2.  ', 'Text', '2024-06-18 15:54:33'),
(5, 2, 'ngt', 'Hello', 'Text', '2024-06-22 01:34:12'),
(6, 2, 'chatbot', 'hello! 👋 I\'m happy to hear from you. How can i help today? 😊', 'Text', '2024-06-22 01:34:15');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `message_group`
--

CREATE TABLE `message_group` (
  `MessageID` int(11) NOT NULL,
  `ConversationID` int(11) NOT NULL,
  `SenderID` varchar(50) NOT NULL,
  `Message` text NOT NULL,
  `MessageType` enum('Text','Image','File') NOT NULL,
  `TimeSend` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `message_group`
--

INSERT INTO `message_group` (`MessageID`, `ConversationID`, `SenderID`, `Message`, `MessageType`, `TimeSend`) VALUES
(6, 6, 'ngt', 'Hello', 'Text', '2024-06-21 23:25:23'),
(7, 6, 'nnn', 'Chào', 'Text', '2024-06-21 23:29:08'),
(8, 6, 'nvh1', 'Chào', 'Text', '2024-06-21 23:36:44');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `message_single`
--

CREATE TABLE `message_single` (
  `MessageID` int(11) NOT NULL,
  `ConversationID` int(11) NOT NULL,
  `SenderID` varchar(50) NOT NULL,
  `Message` text NOT NULL,
  `MessageType` enum('Text','Image','File') NOT NULL,
  `TimeSend` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `message_single`
--

INSERT INTO `message_single` (`MessageID`, `ConversationID`, `SenderID`, `Message`, `MessageType`, `TimeSend`) VALUES
(64, 24, 'nvh1', 'Hello', 'Text', '2024-06-18 06:58:30'),
(65, 24, 'nvh1', 'file:/D:/AdvancedJavaFinalTerm/NHChatApp/Chat_Client/client_storage/Image/LogoVKU.png', 'Image', '2024-06-18 06:59:24'),
(66, 24, 'nvh1', 'file:/D:/AdvancedJavaFinalTerm/NHChatApp/Chat_Client/client_storage/File/text6.txt', 'File', '2024-06-18 07:00:08'),
(67, 24, 'nvh1', 'Hello rảnh ko', 'Text', '2024-06-18 10:39:36'),
(68, 24, 'nvh1', 'Alo', 'Text', '2024-06-18 10:50:16'),
(69, 24, 'nnn', 'Xin chào', 'Text', '2024-06-18 10:50:30'),
(70, 24, 'nnn', 'Có chuyện gì đó', 'Text', '2024-06-18 10:51:30'),
(71, 24, 'nvh1', 'Có lẽ cần một cái gật đầu', 'Text', '2024-06-18 10:51:53'),
(72, 24, 'nvh1', 'Chào buổi tối', 'Text', '2024-06-18 13:17:46'),
(73, 24, 'nnn', 'Chào', 'Text', '2024-06-18 13:18:01'),
(74, 24, 'nvh1', 'Học bài chung nào', 'Text', '2024-06-18 13:18:20'),
(75, 24, 'nvh1', 'Alo', 'Text', '2024-06-18 13:24:12'),
(76, 24, 'nvh1', 'Hú', 'Text', '2024-06-18 13:53:06'),
(77, 24, 'nnn', 'Hello', 'Text', '2024-06-18 13:55:58'),
(78, 24, 'nvh1', 'Sao đó', 'Text', '2024-06-18 13:57:45'),
(79, 24, 'nnn', 'Đi chơi không', 'Text', '2024-06-18 13:58:04'),
(80, 24, 'nvh1', 'Không được', 'Text', '2024-06-18 13:58:19'),
(81, 24, 'nnn', 'CHán vậy trời', 'Text', '2024-06-18 14:02:31'),
(82, 24, 'nvh1', 'Thôi hẹn bữa khác ', 'Text', '2024-06-18 14:04:00'),
(83, 24, 'nnn', 'Oke', 'Text', '2024-06-18 14:04:11'),
(84, 24, 'nvh1', 'Alo đi ăn không', 'Text', '2024-06-18 14:07:56'),
(85, 24, 'nnn', 'Đi ', 'Text', '2024-06-18 14:08:06'),
(86, 24, 'nnn', 'Qua đoán t', 'Text', '2024-06-18 14:14:34'),
(87, 24, 'nvh1', 'Oke chờ tí t qua', 'Text', '2024-06-18 14:15:02'),
(88, 24, 'nnn', 'Oke qua đi', 'Text', '2024-06-18 14:41:23'),
(89, 24, 'nvh1', 'OKe qua đây', 'Text', '2024-06-18 14:41:39'),
(90, 24, 'nvh1', 'Về chưa', 'Text', '2024-06-18 14:48:39'),
(91, 24, 'nvh1', 'Alo', 'Text', '2024-06-18 14:57:23'),
(92, 24, 'nvh1', 'Về chưa đó', 'Text', '2024-06-18 14:57:33'),
(93, 24, 'nvh1', '123', 'Text', '2024-06-18 15:01:42'),
(94, 24, 'nvh1', 'Sao vậy trời', 'Text', '2024-06-18 15:01:56'),
(95, 24, 'nnn', 'Về rồi', 'Text', '2024-06-18 15:07:40'),
(96, 24, 'nnn', 'Alo', 'Text', '2024-06-18 15:40:09'),
(97, 24, 'nnn', 'Bạn về chưa', 'Text', '2024-06-18 15:40:20'),
(98, 24, 'nnn', 'Alo', 'Text', '2024-06-18 15:40:28'),
(99, 24, 'nnn', 'Alo', 'Text', '2024-06-18 15:40:33'),
(100, 24, 'nvh1', 'Xin chào', 'Text', '2024-06-18 15:46:36'),
(101, 24, 'nvh1', 'Thành công rồi này', 'Text', '2024-06-18 15:46:46'),
(102, 24, 'nnn', 'Oke tuyệt vời', 'Text', '2024-06-18 15:47:19'),
(103, 24, 'nnn', 'Hẹn kèo ngày mai nha', 'Text', '2024-06-18 15:47:44'),
(104, 24, 'nvh1', 'Oke được đó', 'Text', '2024-06-18 15:47:49'),
(105, 24, 'nnn', 'file:/D:/AdvancedJavaFinalTerm/NHChatApp/Chat_Client/client_storage/File/text6.txt', 'File', '2024-06-18 15:47:58'),
(106, 24, 'nvh1', 'Gì vậy', 'Text', '2024-06-18 15:48:06'),
(107, 24, 'nnn', 'Tôi gửi thử', 'Text', '2024-06-18 15:48:12'),
(108, 25, 'ngt', 'Hello', 'Text', '2024-06-21 23:20:22'),
(109, 25, 'ngt', 'Hôm nay đi chơi không', 'Text', '2024-06-21 23:20:45'),
(110, 25, 'nvh1', 'Đi', 'Text', '2024-06-21 23:20:56'),
(111, 24, 'nnn', 'Alo', 'Text', '2024-06-23 02:52:16'),
(112, 24, 'nvh1', 'Alo', 'Text', '2024-06-23 02:52:27'),
(113, 24, 'nnn', 'Hello', 'Text', '2024-06-23 03:36:58'),
(114, 24, 'nnn', 'file:/D:/AdvancedJavaFinalTerm/NHChatApp/Chat_Client/client_storage/Image/image_animal.jpg', 'Image', '2024-06-23 04:55:30');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`ClientID`);

--
-- Chỉ mục cho bảng `conversation_bot`
--
ALTER TABLE `conversation_bot`
  ADD PRIMARY KEY (`ConversationID`),
  ADD KEY `chat_client` (`ClientID`);

--
-- Chỉ mục cho bảng `conversation_group`
--
ALTER TABLE `conversation_group`
  ADD PRIMARY KEY (`ConversationID`);

--
-- Chỉ mục cho bảng `conversation_group_members`
--
ALTER TABLE `conversation_group_members`
  ADD PRIMARY KEY (`ConversationID`,`ClientID`),
  ADD KEY `ClientID` (`ClientID`);

--
-- Chỉ mục cho bảng `conversation_single`
--
ALTER TABLE `conversation_single`
  ADD PRIMARY KEY (`ConversationID`),
  ADD KEY `ClientID1` (`ClientID1`),
  ADD KEY `ClientID2` (`ClientID2`);

--
-- Chỉ mục cho bảng `message_bot`
--
ALTER TABLE `message_bot`
  ADD PRIMARY KEY (`MessageID`),
  ADD KEY `bot_client` (`ID`),
  ADD KEY `bot_client_conversation` (`ConversationID`);

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
-- AUTO_INCREMENT cho bảng `conversation_bot`
--
ALTER TABLE `conversation_bot`
  MODIFY `ConversationID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `conversation_group`
--
ALTER TABLE `conversation_group`
  MODIFY `ConversationID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `conversation_single`
--
ALTER TABLE `conversation_single`
  MODIFY `ConversationID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT cho bảng `message_bot`
--
ALTER TABLE `message_bot`
  MODIFY `MessageID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `message_group`
--
ALTER TABLE `message_group`
  MODIFY `MessageID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT cho bảng `message_single`
--
ALTER TABLE `message_single`
  MODIFY `MessageID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=115;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `conversation_bot`
--
ALTER TABLE `conversation_bot`
  ADD CONSTRAINT `chat_client` FOREIGN KEY (`ClientID`) REFERENCES `client` (`ClientID`);

--
-- Các ràng buộc cho bảng `conversation_group_members`
--
ALTER TABLE `conversation_group_members`
  ADD CONSTRAINT `conversation_group_members_ibfk_1` FOREIGN KEY (`ConversationID`) REFERENCES `conversation_group` (`ConversationID`),
  ADD CONSTRAINT `conversation_group_members_ibfk_2` FOREIGN KEY (`ClientID`) REFERENCES `client` (`ClientID`);

--
-- Các ràng buộc cho bảng `conversation_single`
--
ALTER TABLE `conversation_single`
  ADD CONSTRAINT `conversation_single_ibfk_1` FOREIGN KEY (`ClientID1`) REFERENCES `client` (`ClientID`),
  ADD CONSTRAINT `conversation_single_ibfk_2` FOREIGN KEY (`ClientID2`) REFERENCES `client` (`ClientID`);

--
-- Các ràng buộc cho bảng `message_bot`
--
ALTER TABLE `message_bot`
  ADD CONSTRAINT `bot_client_conversation` FOREIGN KEY (`ConversationID`) REFERENCES `conversation_bot` (`ConversationID`);

--
-- Các ràng buộc cho bảng `message_group`
--
ALTER TABLE `message_group`
  ADD CONSTRAINT `message_group_ibfk_1` FOREIGN KEY (`ConversationID`) REFERENCES `conversation_group` (`ConversationID`),
  ADD CONSTRAINT `message_group_ibfk_2` FOREIGN KEY (`SenderID`) REFERENCES `client` (`ClientID`);

--
-- Các ràng buộc cho bảng `message_single`
--
ALTER TABLE `message_single`
  ADD CONSTRAINT `message_single_ibfk_1` FOREIGN KEY (`ConversationID`) REFERENCES `conversation_single` (`ConversationID`),
  ADD CONSTRAINT `message_single_ibfk_2` FOREIGN KEY (`SenderID`) REFERENCES `client` (`ClientID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
