insert into tb_user (name, username, password) values ('Administrador', 'admin', '$2y$10$hJFrs381fCjPnLb.qaeGLeKaM1UozbRr0k8BnS9SVCmtVV583uEam');

insert into tb_product (name, description, price) values ('Arroz', 'Arroz parboilizado', '5.0');
insert into tb_product (name, description, price) values ('Feijão', 'Feijão carioca', '6.5');
insert into tb_product (name, description, price) values ('Macarrão', 'Macarrão parafuso', '3.75');
insert into tb_product (name, description, price) values ('Milho Verde', 'Milho verde em lata', '2.60');
insert into tb_product (name, description, price) values ('Ervilha', 'Ervilha em lata', '2.2');
insert into tb_product (name, description, price) values ('Farinha de Milho', 'Farinha de milho para cuscuz', '1.55');
insert into tb_product (name, description, price) values ('Farinha de Trigo', 'Farinha de trigo com fermento', '3.4');
insert into tb_product (name, description, price) values ('Molho de Tomate', 'Molho de tomate sabor manjericão', '3.15');
insert into tb_product (name, description, price) values ('Grão de Bico', 'Grão de bico tipo 1', '8.5');
insert into tb_product (name, description, price) values ('Açúcar', 'Açúcar demerara', '2.1');

insert into tb_client (name, address, installments_limit, credit_limit) values ('João da Silva', 'São Paulo, Brasil', 5, '500.0');
insert into tb_client (name, address, installments_limit, credit_limit) values ('Carlos Roberto', 'Rio Grande do Norte, Brasil', 8, '1000.0');
insert into tb_client (name, address, installments_limit, credit_limit) values ('Maria Guimarães', 'Mato Grosso, Brasil', 10, '2000.0');

insert into tb_office_hour (day_of_week, start_time, end_time) values ('1', '08:00', '18:00:00');
insert into tb_office_hour (day_of_week, start_time, end_time) values ('2', '08:00', '18:00:00');
insert into tb_office_hour (day_of_week, start_time, end_time) values ('3', '08:00', '18:00:00');
insert into tb_office_hour (day_of_week, start_time, end_time) values ('4', '08:00', '18:00:00');
insert into tb_office_hour (day_of_week, start_time, end_time) values ('5', '08:00', '18:00:00');

insert into tb_discount (description, percentage, installments_limit) values ('Desconto para pagamentos no vencimento', '0.1', '1');