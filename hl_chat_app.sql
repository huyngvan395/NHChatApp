CREATE TABLE `client` (
  `ClientID` varchar(50) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Password` varchar(200) NOT NULL,
  `Image` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `conversation_bot` (
  `ConversationID` int(11) NOT NULL,
  `Chatbot` varchar(20) NOT NULL,
  `ClientID` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
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

--

CREATE TABLE `conversation_group_members` (
  `ConversationID` int(11) NOT NULL,
  `ClientID` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `conversation_single`
--

CREATE TABLE `conversation_single` (
  `ConversationID` int(11) NOT NULL,
  `ClientID1` varchar(50) NOT NULL,
  `ClientID2` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
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