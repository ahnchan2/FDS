-- test not violation
-- user_id : 1
insert into open_account_log (`service_time`,`user_id`,`account_number`) values (date_add(now(), interval -24 hour), 1, '1xx-xxx-xxx');
insert into charge_money_log (`service_time`,`user_id`,`account_number`,`charge_amount`,`bank_account_number`) values (date_add(now(), interval -23 hour), 1, '1xx-xxx-xxx', 300000, 'axx-xxx-xxx');
insert into send_money_log (`service_time`,`sender_id`,`sender_account_number`,`sender_balance_before_send`,`receiver_account_number`,`receiver_id`,`send_amount`) values (date_add(now(), interval -22 hour), 1, '1xx-xxx-xxx', 300000, 'rrr-rrr-rrr', 100, 100000);
insert into receive_money_log (`service_time`,`receiver_id`,`receiver_account_number`,`receiver_balance_before_receive`,`sender_account_number`,`sender_id`,`receive_amount`) values (date_add(now(), interval -21 hour), 1, '1xx-xxx-xxx', 200000, 'sss-sss-sss', 200, 100000);


-- test RuleA violation
-- user_id : 2
-- 카카오머니 서비스 계좌 개설을 하고 1시간 이내, 20만원 충전 후 잔액이 1000원 이하가 되는 경우
insert into open_account_log (`service_time`,`user_id`,`account_number`) values (date_add(now(), interval -120 minute), 2, '2xx-xxx-xxx');
insert into charge_money_log (`service_time`,`user_id`,`account_number`,`charge_amount`,`bank_account_number`) values (date_add(now(), interval -110 minute), 2, '2xx-xxx-xxx', 100000, 'bxx-xxx-xxx');
insert into charge_money_log (`service_time`,`user_id`,`account_number`,`charge_amount`,`bank_account_number`) values (date_add(now(), interval -100 minute), 2, '2xx-xxx-xxx', 50000, 'bxx-xxx-xxx');
insert into send_money_log (`service_time`,`sender_id`,`sender_account_number`,`sender_balance_before_send`,`receiver_account_number`,`receiver_id`,`send_amount`) values (date_add(now(), interval -95 minute), 2, '2xx-xxx-xxx', 150000, 'rrr-rrr-rrr', 100, 149500);
insert into receive_money_log (`service_time`,`receiver_id`,`receiver_account_number`,`receiver_balance_before_receive`,`sender_account_number`,`sender_id`,`receive_amount`) values (date_add(now(), interval -92 minute), 2, '2xx-xxx-xxx', 500, 'sss-sss-sss', 200, 149500);
insert into charge_money_log (`service_time`,`user_id`,`account_number`,`charge_amount`,`bank_account_number`) values (date_add(now(), interval -90 minute), 2, '2xx-xxx-xxx', 50000, 'bxx-xxx-xxx');
insert into charge_money_log (`service_time`,`user_id`,`account_number`,`charge_amount`,`bank_account_number`) values (date_add(now(), interval -80 minute), 2, '2xx-xxx-xxx', 50000, 'bxx-xxx-xxx');
insert into send_money_log (`service_time`,`sender_id`,`sender_account_number`,`sender_balance_before_send`,`receiver_account_number`,`receiver_id`,`send_amount`) values (date_add(now(), interval -75 minute), 2, '2xx-xxx-xxx', 250000, 'rrr-rrr-rrr', 100, 200000);
insert into send_money_log (`service_time`,`sender_id`,`sender_account_number`,`sender_balance_before_send`,`receiver_account_number`,`receiver_id`,`send_amount`) values (date_add(now(), interval -70 minute), 2, '2xx-xxx-xxx', 50000, 'rrr-rrr-rrr', 100, 49500);
insert into receive_money_log (`service_time`,`receiver_id`,`receiver_account_number`,`receiver_balance_before_receive`,`sender_account_number`,`sender_id`,`receive_amount`) values (date_add(now(), interval -60 minute), 2, '2xx-xxx-xxx', 500, 'sss-sss-sss', 200, 149500);
insert into send_money_log (`service_time`,`sender_id`,`sender_account_number`,`sender_balance_before_send`,`receiver_account_number`,`receiver_id`,`send_amount`) values (date_add(now(), interval -50 minute), 2, '2xx-xxx-xxx', 150000, 'rrr-rrr-rrr', 100, 149500);

-- explain
-- select service_time
-- from
-- 	open_account_log
-- where
-- 	user_id = 2 order by service_time desc limit 1;


-- explain
-- select service_time
-- from
-- 	(select service_time, @running_total := @running_total + charge_amount as cumulative_sum
-- 	from
-- 		charge_money_log
-- 		join (select @running_total := 0) tmp
-- 	where
-- 		user_id = 2
-- 		and '2018-03-13 23:17:22' <= service_time
-- 		and service_time < date_add('2018-03-13 23:17:22', interval 1 hour)
-- 	) t
-- where
-- 	cumulative_sum >= 200000
-- order by
-- 	service_time asc
-- limit 1;


-- explain
-- select if(
-- (select count(service_time)
-- 	from
-- 		send_money_log
-- 	where
-- 		sender_id = 2
-- 		and '2018-03-13 23:47:22' <= service_time
-- 		and service_time < date_add('2018-03-13 23:17:22', interval 1 hour)
-- 		and sender_balance_before_send - send_amount <= 1000) > 0
-- , true, false) as result;



-- test RuleB violation
-- user_id : 3
-- 카카오머니 서비스 계좌 개설을 하고 7일 이내, 카카오머니 받기로 10만원 이상 금액을 5회 이상 하는 경우
insert into open_account_log (`service_time`,`user_id`,`account_number`) values (date_add(now(), interval -50 day), 3, '3xx-xxx-xxx');
insert into receive_money_log (`service_time`,`receiver_id`,`receiver_account_number`,`receiver_balance_before_receive`,`sender_account_number`,`sender_id`,`receive_amount`) values (date_add(now(), interval -48 day), 3, '3xx-xxx-xxx', 0, 'sss-sss-sss', 200, 100000);
insert into receive_money_log (`service_time`,`receiver_id`,`receiver_account_number`,`receiver_balance_before_receive`,`sender_account_number`,`sender_id`,`receive_amount`) values (date_add(now(), interval -47 day), 3, '3xx-xxx-xxx', 100000, 'sss-sss-sss', 200, 100000);
insert into receive_money_log (`service_time`,`receiver_id`,`receiver_account_number`,`receiver_balance_before_receive`,`sender_account_number`,`sender_id`,`receive_amount`) values (date_add(now(), interval -46 day), 3, '3xx-xxx-xxx', 200000, 'sss-sss-sss', 200, 100000);
insert into receive_money_log (`service_time`,`receiver_id`,`receiver_account_number`,`receiver_balance_before_receive`,`sender_account_number`,`sender_id`,`receive_amount`) values (date_add(now(), interval -45 day), 3, '3xx-xxx-xxx', 300000, 'sss-sss-sss', 200, 100000);
insert into receive_money_log (`service_time`,`receiver_id`,`receiver_account_number`,`receiver_balance_before_receive`,`sender_account_number`,`sender_id`,`receive_amount`) values (date_add(now(), interval -44 day), 3, '3xx-xxx-xxx', 400000, 'sss-sss-sss', 200, 100000);



-- test RuleC violation
-- user_id : 4
-- 2시간 이내, 카카오머니 받기로 5만원 이상 금액을 3회 이상 하는 경우
insert into open_account_log (`service_time`,`user_id`,`account_number`) values (date_add(now(), interval -50 day), 4, '4xx-xxx-xxx');
insert into receive_money_log (`service_time`,`receiver_id`,`receiver_account_number`,`receiver_balance_before_receive`,`sender_account_number`,`sender_id`,`receive_amount`) values (date_add(now(), interval -100 minute), 4, '4xx-xxx-xxx', 10000, 'sss-sss-sss', 200, 50000);
insert into receive_money_log (`service_time`,`receiver_id`,`receiver_account_number`,`receiver_balance_before_receive`,`sender_account_number`,`sender_id`,`receive_amount`) values (date_add(now(), interval -80 minute), 4, '4xx-xxx-xxx', 60000, 'sss-sss-sss', 200, 50000);
insert into receive_money_log (`service_time`,`receiver_id`,`receiver_account_number`,`receiver_balance_before_receive`,`sender_account_number`,`sender_id`,`receive_amount`) values (date_add(now(), interval -40 minute), 4, '4xx-xxx-xxx', 110000, 'sss-sss-sss', 200, 50000);

                    
