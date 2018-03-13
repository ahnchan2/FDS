-- 카카오머니 서비스 계좌 개설 로그
CREATE TABLE `open_account_log` (
  `service_time` datetime NOT NULL,
  `user_id` int NOT NULL,
  `account_number` varchar(30) NOT NULL,
  key `idx_open_account_log` (`service_time`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 카카오머니 송금 로그
CREATE TABLE `send_money_log` (
  `service_time` datetime NOT NULL,
  `sender_id` int NOT NULL,
  `sender_account_number` varchar(30) NOT NULL,
  `sender_balance_before_send` bigint NOT NULL,
  `receiver_account_number` varchar(30) NOT NULL,
  `receiver_id` int NOT NULL,
  `send_amount` bigint NOT NULL,
  key `idx_send_money_log` (`service_time`, `sender_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 카카오머니 충전 로그 (은행계좌에서 머니 충전)
CREATE TABLE `charge_money_log` (
  `service_time` datetime NOT NULL,
  `user_id` int NOT NULL,
  `account_number` varchar(30) NOT NULL,
  `charge_amount` bigint NOT NULL,
  `bank_account_number` varchar(30) NOT NULL,
  key `idx_charge_money_log` (`service_time`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 카카오머니 받기 로그
CREATE TABLE `receive_money_log` (
  `service_time` datetime NOT NULL,
  `receiver_id` int NOT NULL,
  `receiver_account_number` varchar(30) NOT NULL,
  `receiver_balance_before_receive` bigint NOT NULL,
  `sender_account_number` varchar(30) NOT NULL,
  `sender_id` int NOT NULL,
  `receive_amount` bigint NOT NULL,
  key `idx_receive_money_log` (`service_time`, `receiver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;