
--Changes as on 14/2/2016

ALTER TABLE `voyager`.`tb_corporatecustomer` ADD COLUMN `rembursed` NUMERIC AFTER `status`;

ALTER TABLE `voyager`.`tb_corporatecustomer` ADD COLUMN `rewardPoints` NUMERIC AFTER `rembursed`;

ALTER TABLE `voyager`.`tb_customer` ADD COLUMN `rewardPoints` NUMERIC AFTER `modifiedDate`;


ALTER TABLE `voyager`.`tb_corporatecustomer` DROP COLUMN `fname`,
 DROP COLUMN `mname`,
 DROP COLUMN `lname`,
 DROP COLUMN `secondaryEmail`,
 DROP COLUMN `secondaryPhone`;

 
 
 CREATE TABLE `voyager`.`tb_drlocation` (
  `driverId` INTEGER UNSIGNED NOT NULL,
  `lat` TEXT NOT NULL,
  `lng` TEXT NOT NULL,
  `place` TEXT,
  `subArea` TEXT,
  `area` TEXT,
  `countryCode` VARCHAR(45),
  `zip` VARCHAR(45),
  `zoneCode` VARCHAR(45),
  `vehicleType` VARCHAR(45),
  `serviceType` VARCHAR(45),
  `status` VARCHAR(5),
  `lastUpdateDate` DATETIME NOT NULL,
  PRIMARY KEY (`driverId`)
)
ENGINE = InnoDB;


ALTER TABLE `voyager`.`tb_attrconfig` ADD COLUMN `status` varchar(5) AFTER `sort`;




-----------------DATA CHANGES


insert into tb_drlocation values (1, '19.170098', '72.876286', 'MUMBAI', 'MUMBAI', 'MUBAI', 'IN', '', '', 'TXI', 'TX', 'A', '2016-03-12 00:12:50')

insert into tb_drlocation values (2, '19.170099', '72.876287', 'MUMBAI', 'MUMBAI', 'MUMBAI', 'IN', '', '', 'TXI', 'TX', 'A', '2016-03-15 00:12:50');


insert into tb_attrconfig values
('VEHICLE_TYPE_PRIVATE', 'HUTCH_BACK', 'HTB', '', '', NULL,NULL , '', NULL,NULL , '1');
insert into tb_attrconfig values
('VEHICLE_TYPE_PRIVATE', 'MUV', 'MUV', '', '', NULL,NULL , '',NULL , NULL, '0');
insert into tb_attrconfig values
('VEHICLE_TYPE_PRIVATE', 'SEDAN', 'SED', '', '', NULL,NULL , '', NULL,NULL , '1');
insert into tb_attrconfig values
('VEHICLE_TYPE_PRIVATE', 'SUV', 'SUV', '', '', NULL, NULL, '', NULL, NULL, '1');
insert into tb_attrconfig values
('VEHICLE_TYPE_TAXI', 'MAXI_TAXI', 'MTX', '', '', NULL,NULL , '',NULL ,NULL , '1');
insert into tb_attrconfig values
('VEHICLE_TYPE_TAXI', 'TAXI', 'TXI', '', '',NULL , NULL, '', NULL,NULL , '1');


ALTER TABLE `voyager`.`tb_drvehicle` CHANGE COLUMN `vehicalId` `vehicleId` INT(10) UNSIGNED NOT NULL,
 DROP PRIMARY KEY,
 ADD PRIMARY KEY  USING BTREE(`vehicleId`);
 
 
 ---------------------1.1.1v
 
 ALTER TABLE `voyager`.`tb_drivertripdetails` ADD COLUMN `travelTime` NUMERIC NOT NULL AFTER `feedback`;

 
 insert into tb_attrconfig values
('TARIFF_CODE', 'TARIFF01', '07:00', '18:59', '',NULL , NULL, '', NULL,NULL , '1');

insert into tb_attrconfig values
('TARIFF_CODE', 'TARIFF02', '19:00', '06:59', '',NULL , NULL, '', NULL,NULL , '1');


INSERT INTO `tb_attrconfig` (`masterAttr`, `attrName`, `attrValue1`, `attrValue2`, `type`, `level`, `flag`, `unit`, `zoneId`, `sort`) VALUES
('FARE_TARIFF01', 'BASE_FARE', '1', NULL, NULL, NULL, NULL, '$', 1, NULL),
('FARE_TARIFF01', 'BASE_KM_RATE', '0.1', NULL, NULL, NULL, NULL, '$', 1, NULL),
('FARE_TARIFF01', 'BASE_WAIT_CHRG', '0.1', NULL, NULL, NULL, NULL, '$', 1, NULL),
('FARE_TARIFF01', 'BASE_WAIT_TIME', '15', NULL, NULL, NULL, NULL, 'SEC', 1, NULL),
('FARE_TARIFF01', 'BASE_WAIT_UNIT', '30', NULL, NULL, NULL, NULL, 'SEC', 1, NULL);


INSERT INTO `tb_attrconfig` (`masterAttr`, `attrName`, `attrValue1`, `attrValue2`, `type`, `level`, `flag`, `unit`, `zoneId`, `sort`) VALUES
('FARE_TARIFF02', 'BASE_FARE', '1.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('FARE_TARIFF02', 'BASE_KM_RATE', '0.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('FARE_TARIFF02', 'BASE_WAIT_CHRG', '0.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('FARE_TARIFF02', 'BASE_WAIT_TIME', '15', NULL, NULL, NULL, NULL, 'SEC', 1, NULL),
('FARE_TARIFF02', 'BASE_WAIT_UNIT', '30', NULL, NULL, NULL, NULL, 'SEC', 1, NULL);

update tb_attrconfig set masterAttr = 'TARIFF01' where masterAttr = 'FARE_TARIFF01';
update tb_attrconfig set masterAttr = 'TARIFF02' where masterAttr = 'FARE_TARIFF02';


ALTER TABLE `voyager`.`tb_attrconfig` MODIFY COLUMN `type` VARCHAR(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL;


insert into tb_attrconfig values
('FARE', 'BASE_KM', '2', NULL, '',NULL , NULL, '', NULL,NULL , 'A');

insert into tb_attrconfig values
('TARIFF01', 'BASE_KM', '2', NULL, '',NULL , NULL, '', 1,NULL , 'A');

insert into tb_attrconfig values
('TARIFF02', 'BASE_KM', '2', NULL, '',NULL , NULL, '', 1,NULL , 'A');


insert into tb_attrconfig values
('FARE', 'BOOKING_FEE', '2', NULL, '',NULL , NULL, '', 1,NULL , 'A');

update set tb_attrconfig set attrName = 'BASE_KM_FARE' where attrName = 'BASE_KM_RATE';


ALTER TABLE `voyager`.`tb_customerbooking` MODIFY COLUMN `bookingId` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT;


INSERT INTO `tb_attrconfig` (`masterAttr`, `attrName`, `attrValue1`, `attrValue2`, `type`, `level`, `flag`, `unit`, `zoneId`, `sort`) VALUES
('TARIFF02_TXI', 'BASE_FARE', '1.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_TXI', 'BASE_KM_RATE', '0.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_TXI', 'BASE_WAIT_CHRG', '0.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_TXI', 'BASE_WAIT_TIME', '15', NULL, NULL, NULL, NULL, 'SEC', 1, NULL),
('TARIFF02_TXI', 'BASE_WAIT_UNIT', '30', NULL, NULL, NULL, NULL, 'SEC', 1, NULL);


INSERT INTO `tb_attrconfig` (`masterAttr`, `attrName`, `attrValue1`, `attrValue2`, `type`, `level`, `flag`, `unit`, `zoneId`, `sort`) VALUES
('TARIFF02_MTX', 'BASE_FARE', '1.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_MTX', 'BASE_KM_RATE', '0.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_MTX', 'BASE_WAIT_CHRG', '0.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_MTX', 'BASE_WAIT_TIME', '15', NULL, NULL, NULL, NULL, 'SEC', 1, NULL),
('TARIFF02_MTX', 'BASE_WAIT_UNIT', '30', NULL, NULL, NULL, NULL, 'SEC', 1, NULL);


INSERT INTO `tb_attrconfig` (`masterAttr`, `attrName`, `attrValue1`, `attrValue2`, `type`, `level`, `flag`, `unit`, `zoneId`, `sort`) VALUES
('TARIFF02_SED', 'BASE_FARE', '1.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_SED', 'BASE_KM_RATE', '0.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_SED', 'BASE_WAIT_CHRG', '0.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_SED', 'BASE_WAIT_TIME', '15', NULL, NULL, NULL, NULL, 'SEC', 1, NULL),
('TARIFF02_SED', 'BASE_WAIT_UNIT', '30', NULL, NULL, NULL, NULL, 'SEC', 1, NULL);

INSERT INTO `tb_attrconfig` (`masterAttr`, `attrName`, `attrValue1`, `attrValue2`, `type`, `level`, `flag`, `unit`, `zoneId`, `sort`) VALUES
('TARIFF02_HTB', 'BASE_FARE', '1.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_HTB', 'BASE_KM_RATE', '0.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_HTB', 'BASE_WAIT_CHRG', '0.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_HTB', 'BASE_WAIT_TIME', '15', NULL, NULL, NULL, NULL, 'SEC', 1, NULL),
('TARIFF02_HTB', 'BASE_WAIT_UNIT', '30', NULL, NULL, NULL, NULL, 'SEC', 1, NULL);

INSERT INTO `tb_attrconfig` (`masterAttr`, `attrName`, `attrValue1`, `attrValue2`, `type`, `level`, `flag`, `unit`, `zoneId`, `sort`) VALUES
('TARIFF02_SUV', 'BASE_FARE', '1.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_SUV', 'BASE_KM_RATE', '0.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_SUV', 'BASE_WAIT_CHRG', '0.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_SUV', 'BASE_WAIT_TIME', '15', NULL, NULL, NULL, NULL, 'SEC', 1, NULL),
('TARIFF02_SUV', 'BASE_WAIT_UNIT', '30', NULL, NULL, NULL, NULL, 'SEC', 1, NULL);

INSERT INTO `tb_attrconfig` (`masterAttr`, `attrName`, `attrValue1`, `attrValue2`, `type`, `level`, `flag`, `unit`, `zoneId`, `sort`) VALUES
('TARIFF02_MUV', 'BASE_FARE', '1.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_MUV', 'BASE_KM_RATE', '0.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_MUV', 'BASE_WAIT_CHRG', '0.5', NULL, NULL, NULL, NULL, '$', 1, NULL),
('TARIFF02_MUV', 'BASE_WAIT_TIME', '15', NULL, NULL, NULL, NULL, 'SEC', 1, NULL),
('TARIFF02_MUV', 'BASE_WAIT_UNIT', '30', NULL, NULL, NULL, NULL, 'SEC', 1, NULL);



-------------------------
update `tb_attrconfig`  set status = 'A' ;

update tb_attrconfig set level = 1 ;
update tb_attrconfig set level = 2, type= 'TARIFF' where masterAttr like 'TARIFF_%' ;
update tb_attrconfig set level = 2 where masterAttr like 'SERVICE_TYPE' ;

update tb_attrconfig set level = 1 ;
update tb_attrconfig set level = 2, type= 'TARIFF' where masterAttr like 'TARIFF_%' ;
update tb_attrconfig set level = 2 where type like 'SERVICE_TYPE' ;

delete from tb_attrconfig where masterAttr in ('TARIFF01','TARIFF02') ;

update tb_attrconfig set masterAttr = 'TARIFF' where masterAttr = 'TARIFF_CODE' ;

update tb_attrconfig set level = 1, type= NULL where masterAttr = 'TARIFF' ;

update tb_attrconfig set masterAttr = REPLACE(masterAttr,'_','-') where masterAttr like 'TARIFF_%';


ALTER TABLE `voyager`.`tb_driverdetails` DROP INDEX `cseCode`,
 ADD UNIQUE INDEX `driverId` USING BTREE(`driverId`);


ALTER TABLE `voyager`.`tb_userrole` MODIFY COLUMN `status` VARCHAR(5) DEFAULT NULL;
ALTER TABLE `voyager`.`tb_users` ADD COLUMN `corpCustId` INTEGER UNSIGNED AFTER `phone`;


ALTER TABLE `voyager`.`tb_corpemp` ADD COLUMN `pin` VARCHAR(45) NOT NULL AFTER `createDate`;

 
 
 
 
 ----------------------------------Mihir DB CHANGES  ---05032016
 
 ALTER TABLE `voyager`.`tb_driverdetails` CHANGE COLUMN `driverDetailId` `driverDetaiId` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
 MODIFY COLUMN `driverId` INTEGER NOT NULL DEFAULT 0;
 
 
 
 ALTER TABLE `voyager`.`tb_driver` ADD COLUMN `createDate` DATETIME AFTER `deleteflag`,
 ADD COLUMN `modifiedDate` DATETIME AFTER `createDate`,
 ADD COLUMN `createdBy` INTEGER UNSIGNED AFTER `modifiedDate`,
 ADD COLUMN `modifiedBy` INTEGER UNSIGNED AFTER `createdBy`;

 
 ALTER TABLE `voyager`.`tb_enquiryform` ADD COLUMN `driveWith` VARCHAR(100) AFTER `cseType4`;

 
 
 ALTER TABLE `voyager`.`tb_customeraccount` MODIFY COLUMN `status` VARCHAR(5) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
 ADD COLUMN `defaultAccount` TINYINT(3) UNSIGNED AFTER `deleteflag`,
 ADD COLUMN `corpCustId` INTEGER UNSIGNED AFTER `defaultAccount`;

 
 ALTER TABLE `voyager`.`tb_customerridedetails` ADD COLUMN `tollCharges` DECIMAL(10,2) AFTER `deleteflag`;

 ALTER TABLE `voyager`.`tb_customerridedetails` ADD COLUMN `status` VARCHAR(5) AFTER `deleteflag`;

 
 
 -------------------
 
 ALTER TABLE `voyager`.`tb_userrole` CHANGE COLUMN  `ID`  `roleId` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
 DROP PRIMARY KEY,
 ADD PRIMARY KEY  USING BTREE(`roleId`);
 
 
 
 
 
 
 
 
 ---1.1  build 1 V

 ALTER TABLE `voyager`.`tb_customerridedetails` ADD COLUMN `waitTime` DECIMAL(10,2) AFTER `tollCharges`;
/**
 CREATE TABLE `voyager`.`tb_gcmregid` (
  `driverId` INTEGER UNSIGNED NOT NULL,
  `gcmRegId` TEXT NOT NULL,
  `status` VARCHAR(5) NOT NULL,
  PRIMARY KEY (`driverId`)
)
ENGINE = InnoDB;
**/
 
 --ALTER TABLE `voyager`.`tb_drlocation` ADD COLUMN `gcmRegId` TEXT NOT NULL AFTER `status`;
ALTER TABLE `voyager`.`tb_drlocation` ADD COLUMN `gcmRegId` TEXT  NULL AFTER `status`;



ALTER TABLE `voyager`.`tb_customerbooking` ADD COLUMN `gcmRegId` TEXT AFTER `destPlace`;



ALTER TABLE `voyager`.`tb_drlocation` ADD COLUMN `vehicleId` INTEGER UNSIGNED AFTER `gcmRegId`;



ALTER TABLE `tb_driver` CHANGE `password` `password` VARCHAR(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL;

ALTER TABLE `tb_drvehicle` CHANGE `vehicleId` `vehicleId` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT;

ALTER TABLE `tb_corporatecustomer` CHANGE `rembursed` `rembursed` INT(2) NULL DEFAULT NULL;


----------------


ALTER TABLE `voyager`.`tb_driverbooking` DROP COLUMN `driverBookingId`,
 DROP PRIMARY KEY,
 ADD PRIMARY KEY  USING BTREE(`driverId`, `bookingId`);

 
 ALTER TABLE `voyager`.`tb_driver` CHANGE COLUMN `serviceType` `serviceTypeDriver` VARCHAR(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL;

 ALTER TABLE `voyager`.`tb_driver` CHANGE COLUMN `referralAppdate` `referralAppDate` DATETIME ;

--TODO today For RefAppDate 

 ALTER TABLE `voyager`.`tb_driver` CHANGE COLUMN `deleteflag` `deleteFlag` TINYINT(4) DEFAULT 0;
ALTER TABLE `voyager`.`tb_customerbooking` ADD COLUMN `pin` VARCHAR(5) NOT NULL AFTER `gcmRegId`;
  update tb_attrconfig t set attrName  ='BASE_KM_FARE' where attrName ='BASE_KM_RATE';
  
  ALTER TABLE `voyager`.`tb_customerridedetails` DROP COLUMN `baseFareCharges`;
ALTER TABLE `voyager`.`tb_customerridedetails` MODIFY COLUMN `kmCharges` DECIMAL(10,0) DEFAULT NULL,
 MODIFY COLUMN `rideTotalAmt` DECIMAL(10,2) DEFAULT NULL;
 
ALTER TABLE `voyager`.`tb_customerridedetails` MODIFY COLUMN `tariffCode` VARCHAR(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL;


CREATE TABLE `voyager`.`tb_bookinglocation` (
  `bookingId` INTEGER UNSIGNED NOT NULL,
  `lat` VARCHAR(45) NOT NULL,
  `lng` VARCHAR(45) NOT NULL,
  `time` DATETIME NOT NULL,
  PRIMARY KEY (`bookingId`)
)
ENGINE = InnoDB;

CREATE TABLE `voyager`.`tb_driverLogin` (
  `driverId` INTEGER UNSIGNED NOT NULL,
  `loginTime` DATETIME NOT NULL,
  `deviceId` VARCHAR(45),
  `status` VARCHAR(5),
  PRIMARY KEY (`driverId`)
)
ENGINE = InnoDB;

ALTER TABLE `voyager`.`tb_bookinglocation` RENAME TO `voyager`.`tb_drbookinglocation`,
 CHANGE COLUMN `time` `locTime` DATETIME NOT NULL;

 
 
 CREATE TRIGGER t_plot_ride_distance AFTER UPDATE ON tb_drlocation
FOR EACH ROW
BEGIN
  DECLARE bookingId INT;

  IF new.status = 'R' THEN
    SELECT max(bookingId) into bookingId from tb_driverbooking where status = 'BKD' and driverId = new.driverId;
      IF bookingId != null && bookingId > 0 THEN
        INSERT INTO tb_drbookinglocation (bookingId, lat, lng, locTime)
        values ( @bookingId, lat, lng, now())
      END IF
  END IF
END	


--update tb_drlocation set lat =4, lng=5, status = 'R' where driverID =1


DROP TABLE IF EXISTS `voyager`.`tb_equipment`;
CREATE TABLE  `voyager`.`tb_equipment` (
  `assetId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `createBy` varchar(255) DEFAULT NULL,
  `createDate` varchar(255) DEFAULT NULL,
  `modifiedBy` varchar(255) DEFAULT NULL,
  `modifiedDate` varchar(255) DEFAULT NULL,
  `seralNumber` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`assetId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



ALTER TABLE `voyager`.`tb_equipment` MODIFY COLUMN `createBy` INTEGER UNSIGNED DEFAULT NULL,
 MODIFY COLUMN `createDate` DATETIME DEFAULT NULL,
 MODIFY COLUMN `modifiedBy` INTEGER UNSIGNED DEFAULT NULL,
 MODIFY COLUMN `modifiedDate` DATETIME DEFAULT NULL,
 ADD COLUMN `modelName` VARCHAR(100) AFTER `status`,
 ADD COLUMN `description` VARCHAR(255) AFTER `modelName`;
 
 
 DROP TABLE IF EXISTS `voyager`.`tb_eqp_driver`;
CREATE TABLE  `voyager`.`tb_eqp_driver` (
  `driverId` int(10) unsigned NOT NULL,
  `assetId` int(10) unsigned NOT NULL,
  `createDate` datetime NOT NULL,
  `deleteFlag` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`driverId`,`assetId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


ALTER TABLE `tb_equipment` CHANGE `status` `status` VARCHAR(5) NULL DEFAULT NULL;


CREATE TABLE IF NOT EXISTS `tb_eqp_driver_audit` (
  `driverId` int(10) unsigned NOT NULL,
  `assetId` int(10) unsigned NOT NULL,
  `modifiedDate` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



ALTER TABLE `tb_equipment` CHANGE `seralNumber` `serialNumber` VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL;

INSERT INTO `voyager`.`tb_attrconfig` (`masterAttr`, `attrName`, `attrValue1`, `attrValue2`, `type`, `level`, `flag`, `unit`, `zoneId`, `sort`, `status`) VALUES ('ASSET', 'SECURITY', 'CAMERA', NULL, NULL, '1', NULL, NULL, '1', NULL, 'A'), ('ASSET', 'DEVICE', 'ANDROID', NULL, NULL, '1', NULL, NULL, '1', NULL, 'A');

ALTER TABLE `tb_customerridedetails` ADD `currency` VARCHAR(10) NOT NULL ;


-------mihir changes-------------

ALTER TABLE `voyager`.`tb_driverearnings` MODIFY COLUMN `fareTotal` DECIMAL(18,2) NOT NULL DEFAULT 0,
 MODIFY COLUMN `tollTotal` DECIMAL(18,2) NOT NULL DEFAULT 0,
 MODIFY COLUMN `otherExp` DECIMAL(18,2) NOT NULL DEFAULT 0,
 MODIFY COLUMN `bookingFeeTotal` DECIMAL(18,2) NOT NULL DEFAULT 0,
 MODIFY COLUMN `voyagerFee` DECIMAL(18,2) NOT NULL DEFAULT 0,
 MODIFY COLUMN `driverEarning` DECIMAL(18,2) NOT NULL DEFAULT 0,
 ADD COLUMN `driverId` INTEGER UNSIGNED NOT NULL DEFAULT 0 AFTER `earningId`;
 
 ALTER TABLE `tb_driverearnings` CHANGE `driverId` `driverId` INT(11) UNSIGNED NOT NULL;
 
 ALTER TABLE `tb_equipment` CHANGE `status` `status` VARCHAR(5) NULL DEFAULT NULL;


CREATE TABLE IF NOT EXISTS `tb_eqp_driver_audit` (
  `driverId` int(10) unsigned NOT NULL,
  `assetId` int(10) unsigned NOT NULL,
  `modifiedDate` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




ALTER TABLE `tb_equipment` CHANGE `seralNumber` `serialNumber` VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL;

INSERT INTO `voyager`.`tb_attrconfig` (`masterAttr`, `attrName`, `attrValue1`, `attrValue2`, `type`, `level`, `flag`, `unit`, `zoneId`, `sort`, `status`) VALUES ('ASSET', 'SECURITY', 'CAMERA', NULL, NULL, '1', NULL, NULL, '1', NULL, 'A'), ('ASSET', 'DEVICE', 'ANDROID', NULL, NULL, '1', NULL, NULL, '1', NULL, 'A');

ALTER TABLE `tb_drivertripdetails`  ADD `kmTraveledGDM` DECIMAL(18,2) NULL ;

UPDATE `voyager`.`tb_attrconfig` SET `attrName` = 'CAMERA', `attrValue1` = 'CAM' WHERE `tb_attrconfig`.`masterAttr` = 'ASSET' AND `tb_attrconfig`.`attrName` = 'SECURITY';
ALTER TABLE `tb_drivertripdetails` ADD `rideFare` DECIMAL(10,2) NOT NULL AFTER `driverRewards`;

ALTER TABLE `voyager`.`tb_drivertripdetails` ADD COLUMN `drPaymentStatus` VARCHAR(5) NOT NULL DEFAULT 'PEND' AFTER `kmTraveledGDM`
, ROW_FORMAT = DYNAMIC;


ALTER TABLE `voyager`.`tb_driver` MODIFY COLUMN `cseCode` VARCHAR(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL;


ALTER TABLE `voyager`.`tb_drvehicle` MODIFY COLUMN `carNumber` VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL;


ALTER TABLE `voyager`.`tb_driver` DROP INDEX `cseCode`;


ALTER TABLE `voyager`.`tb_customer` ADD COLUMN `gcmRegId` TEXT AFTER `pinDate`;

ALTER TABLE  `tb_customerridedetails` ADD  `currency` VARCHAR( 50 ) NULL AFTER  `waitTime` ;


ALTER TABLE  `tb_drivertripdetails` CHANGE  `vehicalId`  `vehicleId` INT( 10 ) UNSIGNED NOT NULL ;


UPDATE  `tb_drvehicle` SET STATUS =  'A'

ALTER TABLE  `tb_drvehicle` CHANGE  `status`  `status` VARCHAR( 5 ) NOT NULL DEFAULT 'A';

insert into tb_supportmaster  (select supportId,'DRIVER', supportType, supportQuestion, description, now() from tb_supportmaster);

ALTER TABLE `voyager`.`tb_driver` DROP COLUMN `serviceTypeDriver`;


ALTER TABLE `voyager`.`tb_corporatecustomer` ADD COLUMN `modeOfPayment` VARCHAR(5) NOT NULL AFTER `rewardPoints`;
ALTER TABLE `voyager`.`tb_corporatecustomer` MODIFY COLUMN `modeOfPayment` VARCHAR(5) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL;
ALTER TABLE `voyager`.`tb_corporatecustomer` MODIFY COLUMN `status` VARCHAR(5) DEFAULT 0;
update tb_corporatecustomer set status ='A';

/**  TMP*
 * 
 * 
 *//
 
 ALTER TABLE `voyager`.`tb_equipment` ADD COLUMN `assetType` VARCHAR(100) AFTER `description`,
 ADD COLUMN `prodCode` VARCHAR(100) AFTER `assetType`,
 ADD COLUMN `deleteFlag` VARCHAR(5) AFTER `prodCode`;

 ALTER TABLE `voyager`.`tb_corpemp` ADD COLUMN `status` VARCHAR(5) AFTER `pin`;
