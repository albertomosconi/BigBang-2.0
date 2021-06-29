SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ProgTIW
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `ProgTIW` ;
CREATE SCHEMA IF NOT EXISTS `ProgTIW` DEFAULT CHARACTER SET utf8;
USE `ProgTIW` ;

-- -----------------------------------------------------
-- Table `item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `item` ;
CREATE TABLE IF NOT EXISTS `item` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` TEXT NOT NULL,
  `category` TEXT NOT NULL,
  `picture` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

INSERT INTO `item` (`id`,`name`,`description`,`category`,`picture`) VALUES (1,'Animal Farm',"Animal Farm is an allegorical novella by George Orwell, first published in England on 17 August 1945. The book tells the story of a group of farm animals who rebel against their human farmer, hoping to create a society where the animals can be equal, free, and happy. Ultimately, however, the rebellion is betrayed, and the farm ends up in a state as bad as it was before, under the dictatorship of a pig named Napoleon.",'Books','animal_farm.jpg');
INSERT INTO `item` (`id`,`name`,`description`,`category`,`picture`) VALUES (2,'Best T-Shirt ever',"A shirt is a cloth garment for the upper body (from the neck to the waist). Originally an undergarment worn exclusively by men, it has become, in American English, a catch-all term for a broad variety of upper-body garments and undergarments. In British English, a shirt is more specifically a garment with a collar, sleeves with cuffs, and a full vertical opening with buttons or snaps (North Americans would call that a \"dress shirt\", a specific type of collared shirt). A shirt can also be worn with a necktie under the shirt collar.",'Clothing','best_tshirt.jpg');
INSERT INTO `item` (`id`,`name`,`description`,`category`,`picture`) VALUES (3,'Farenheit 451',"Fahrenheit 451 is a 1953 dystopian novel by American writer Ray Bradbury. Often regarded as one of his best works, the novel presents a future American society where books are outlawed and \"firemen\" burn any that are found. The book's tagline explains the title as \"'the temperature at which book paper catches fire, and burns\": the autoignition temperature of paper. The lead character, Guy Montag, is a fireman who becomes disillusioned with his role of censoring literature and destroying knowledge, eventually quitting his job and committing himself to the preservation of literary and cultural writings.",'Books','fahrenheit451.jpg');
INSERT INTO `item` (`id`,`name`,`description`,`category`,`picture`) VALUES (4,'Keyboard',"The technology of computer keyboards includes many elements. Among the more important of these is the switch technology that they use. Computer alphanumeric keyboards typically have 80 to 110 durable switches, generally one for each key. The choice of switch technology affects key response (the positive feedback that a key has been pressed) and pre-travel (the distance needed to push the key to enter a character reliably). Virtual keyboards on touch screens have no physical switches and provide audio and haptic feedback instead. Some newer keyboard models use hybrids of various technologies to achieve greater cost savings or better ergonomics. The modern keyboard also includes a control processor and indicator lights to provide feedback to the user (and to the central processor) about what state the keyboard is in. Plug and play technology means that its 'out of the box' layout can be notified to the system, making the keyboard immediately ready to use without need for further configuration unless the user so desires.",'Technology','keyboard.jpg');
INSERT INTO `item` (`id`,`name`,`description`,`category`,`picture`) VALUES (5,'Monitor',"A computer monitor is an output device that displays information in pictorial form. A monitor usually comprises the visual display, circuitry, casing, and power supply. The display device in modern monitors is typically a thin film transistor liquid crystal display (TFT-LCD) with LED backlighting having replaced cold-cathode fluorescent lamp (CCFL) backlighting. Previous monitors used a cathode ray tube (CRT). Monitors are connected to the computer via VGA, Digital Visual Interface (DVI), HDMI, DisplayPort, USB-C, low-voltage differential signaling (LVDS) or other proprietary connectors and signals. Originally, computer monitors were used for data processing while television sets were used for entertainment. From the 1980s onwards, computers (and their monitors) have been used for both data processing and entertainment, while televisions have implemented some computer functionality. The common aspect ratio of televisions, and computer monitors, has changed from 4:3 to 16:10, to 16:9.",'Technology','monitor.jpg');
INSERT INTO `item` (`id`,`name`,`description`,`category`,`picture`) VALUES (6,'Socks',"A sock is a piece of clothing worn on the feet and often covering the ankle or some part of the calf. Some type of shoe or boot is typically worn over socks. In ancient times, socks were made from leather or matted animal hair. In the late 16th century, machine-knit socks were first produced. Until 1800 both hand knitting and machine knitting were used to produce socks, but after 1800, machine knitting became the predominant method.",'Clothing','socks.png');
INSERT INTO `item` (`id`,`name`,`description`,`category`,`picture`) VALUES (7,'Gelato',"Gelato is a popular frozen dessert of Italian origin. It is generally made with a base of 3.25% butterfat whole milk and sugar. It is generally lower in fat than other styles of frozen desserts. Gelato typically contains 70% less air and more flavouring than other kinds of frozen desserts, giving it a density and richness that distinguishes it from other ice creams. Gelato in its modern form is credited to the Italian chef Francesco Procopio dei Coltelli who in the late 1600s opened his \"Café Procope\" in Paris and introduced gelato at his café, earning notability first in Paris and then in the rest of Europe. Thanks to his gelato, Procopio not only obtained French citizenship, but also got an exclusive royal licence issued by King Louis XIV, making him at the time the sole producer of the frozen dessert in the kingdom.",'Food','gelato.jpg');
INSERT INTO `item` (`id`,`name`,`description`,`category`,`picture`) VALUES (8,'The robots of dawn',"The Robots of Dawn is a \"whodunit\" science fiction novel by American writer Isaac Asimov, first published in 1983. It is the third novel in Asimov's Robot series. The Robot series is a series of 37 science fiction short stories and six novels by American writer Isaac Asimov, featuring positronic robots. Isaac Asimov (/ˈæzɪmɒv/; c.  January 2, 1920 – April 6, 1992) was an American writer and professor of biochemistry at Boston University. He was known for his works of science fiction and popular science. Asimov was a prolific writer, and wrote or edited more than 500 books. He also wrote an estimated 90,000 letters and postcards.",'Books','robots_of_dawn.jpg');
INSERT INTO `item` (`id`,`name`,`description`,`category`,`picture`) VALUES (9,'Pizza',"Pizza is a savory dish of Italian origin consisting of a usually round, flattened base of leavened wheat-based dough topped with tomatoes, cheese, and often various other ingredients (such as anchovies, mushrooms, onions, olives, pineapple, meat, etc.), which is then baked at a high temperature, traditionally in a wood-fired oven. A small pizza is sometimes called a pizzetta. A person who makes pizza is known as a pizzaiolo. The term pizza was first recorded in the 10th century in a Latin manuscript from the Southern Italian town of Gaeta in Lazio, on the border with Campania. Modern pizza was invented in Naples, and the dish and its variants have since become popular in many countries. It has become one of the most popular foods in the world and a common fast food item in Europe and North America, available at pizzerias (restaurants specializing in pizza), restaurants offering Mediterranean cuisine, and via pizza delivery. Many companies sell ready-baked frozen pizzas to be reheated in an ordinary home oven.",'Food','pizza.jpg');
INSERT INTO `item` (`id`,`name`,`description`,`category`,`picture`) VALUES (42,'The Hitchhiker\'s Guide to the Galaxy',"The Hitchhiker's Guide to the Galaxy (sometimes referred to as HG2G, HHGTTG, H2G2, or tHGttG) is a comedy science fiction franchise created by Douglas Adams. Originally a 1978 radio comedy broadcast on BBC Radio 4, it was later adapted to other formats, including stage shows, novels, comic books, a 1981 TV series, a 1984 video game, and 2005 feature film.",'Books','hitchhikers_guide_to_the_galaxy.jpg');
INSERT INTO `item` (`id`,`name`,`description`,`category`,`picture`) VALUES (10,'Murder on the Orient Express',"Murder on the Orient Express is a work of detective fiction by English writer Agatha Christie featuring the Belgian detective Hercule Poirot. It was first published in the United Kingdom by the Collins Crime Club on 1 January 1934. In the United States, it was published on 28 February 1934, under the title of Murder in the Calais Coach, by Dodd, Mead and Company. The UK edition retailed at seven shillings and sixpence (7/6) and the US edition at $2. The elegant train of the 1930s, the Orient Express, is stopped by heavy snowfall. A murder is discovered, and Poirot's trip home to London from the Middle East is interrupted to solve the case.",'Books','murder_on_the_orient_express.jpg');
INSERT INTO `item` (`id`,`name`,`description`,`category`,`picture`) VALUES (11,'A cool hat',"A hat is a head covering which is worn for various reasons, including protection against weather conditions, ceremonial reasons such as university graduation, religious reasons, safety, or as a fashion accessory. In the past, hats were an indicator of social status. In the military, hats may denote nationality, branch of service, rank or regiment. Police typically wear distinctive hats such as peaked caps or brimmed hats, such as those worn by the Royal Canadian Mounted Police. Some hats have a protective function.",'Clothing','cool_hat.jpg');
INSERT INTO `item` (`id`, `name`, `description`, `category`, `picture`) VALUES ('31', 'Pen with stylush con stilo', 'The Celena ballpoint pen is equipped on the button, with a practical stylus that will allow you to use it on all touchscreen devices without neglecting annoying fingerprints. It is made of high-quality metal and features a Soft-Touch barrel for easy writing. It is also finished with brilliant chromatic elements. Durable laser engraving will turn this ballpoint pen into a truly one-of-a-kind specimen. Celena ballpoint pen is equipped on the button, with a practical stylus that will allow you to use it on all touchscreen devices without neglecting annoying fingerprints. It is made of high-quality metal and features a Soft-Touch barrel for easy writing. It is also finished with brilliant chromatic elements. Durable laser engraving will turn this ballpoint pen into a truly one-of-a-kind specimen.', 'Stationery', 'CELENASTY~BLACK0003_enlarged.jpg');
INSERT INTO `item` (`id`, `name`, `description`, `category`, `picture`) VALUES ('32', 'Daily agenda', 'German diary known for the very wide range of colors used for the realization of their diaries. Hard cover, with elastic to close it. A5 format - 12 months 2021. One page per day with space for notes at the bottom of the page. 80 gr ivory paper. Convenient and practical pocket to hold notes. Days in English.', 'Stationery', 'agenda-settimanale-leuchtturm1917.jpg');
INSERT INTO `item` (`id`, `name`, `description`, `category`, `picture`) VALUES ('18', 'Candle', 'MADE WITH SOY WAX - This candle is made with eco-friendly soy wax. It is vegan, paraben free and cruelty free!\nLONG LASTING BURN - Our candles have one of the longest burn times in the market! QUALITY SCENTS- We are extremely selective! HANDCRAFTED HERE IN AMERICA- We take pride in having our candles poured in the USA and helping our economy by generating employment for our people! PERFECT GIFT- Makes a perfect gift for friends, family members, or for yourself!', 'Home', '71zHiJq0XfL._AC_SL1500_.jpg');



-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user` ;
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(60) NOT NULL,
  `password` VARCHAR(256) NOT NULL,
  `address` VARCHAR(60) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

INSERT INTO `user` (`id`,`name`,`surname`,`email`,`password`,`address`) VALUES (1,'Sofia','Martellozzo','sofia.martellozzo@mail.polimi.it','9df9f680dba6c3f53908aae89dd7efb4c6850f85203c9dfd8d99c14063c78556','via , Milano');
INSERT INTO `user` (`id`,`name`,`surname`,`email`,`password`,`address`) VALUES (2,'Margherita','Musumeci','margherita.musumeci@mail.polimi.it','a3355922718b15dea30b5bb5ba073288afbbae25aba42f00f27e3b17f1aafd5c','via , Milano');
INSERT INTO `user` (`id`,`name`,`surname`,`email`,`password`,`address`) VALUES (3,'Alberto Maria','Mosconi','alberto.mosconi@mail.polimi.it','0a2ddaeb1848cb8522247155bda16bff428e00acf9d34c235c955dad85059a60','via , Pavia');
INSERT INTO `user` (`id`,`name`,`surname`,`email`,`password`,`address`) VALUES (4,'Pluto','Manzo','a@a.com','80084bf2fba02475726feb2cab2d8215eab14bc6bdd8bfb2c8151257032ecd8b','via , Topolinia');

-- -----------------------------------------------------
-- Table `order_info`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `order_info` ;
CREATE TABLE IF NOT EXISTS `order_info` (
  `id` CHAR(36) NOT NULL,
  `id_user` INT NOT NULL,
  `id_vendor` INT NOT NULL,
  `date` TIMESTAMP NOT NULL,
  `shipping_cost` FLOAT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `order_info_id_user`
    FOREIGN KEY (`id_user`)
    REFERENCES `user` (`id`)
    ON UPDATE CASCADE,
  CONSTRAINT `order_info_id_vendor`
    FOREIGN KEY (`id_vendor`)
    REFERENCES `vendor` (`id`)
    ON UPDATE CASCADE)
ENGINE = InnoDB;

INSERT INTO `order_info` (`id`,`id_user`,`id_vendor`,`date`,`shipping_cost`) VALUES ('1e6ff61f-e059-410f-8410-3aa3cb116236',4,1,timestamp("2021-01-26","18:32:11"),4.2);
INSERT INTO `order_info` (`id`,`id_user`,`id_vendor`,`date`,`shipping_cost`) VALUES ('85ab8b69-cc90-4852-a29f-0f16e31dc1f4',4,2,timestamp("2021-02-02","13:02:40"),0);
INSERT INTO `order_info` (`id`,`id_user`,`id_vendor`,`date`,`shipping_cost`) VALUES ('7457aa24-387b-456a-9e68-c7a8bfbc32bb',4,3,timestamp("2021-02-18","01:26:51"),24.99);
INSERT INTO `order_info` (`id`,`id_user`,`id_vendor`,`date`,`shipping_cost`) VALUES ('b9e97e78-972c-473c-a679-6f8d057adf61',4,1,timestamp("2021-03-14","15:09:26"),3.10);

-- -----------------------------------------------------
-- Table `ordered_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ordered_item` ;
CREATE TABLE IF NOT EXISTS `ordered_item` (
  `id_order` CHAR(36) NOT NULL,
  `id_item` INT NOT NULL,
  `quantity` INT NOT NULL,
  `cost` FLOAT NOT NULL,
  PRIMARY KEY (`id_order`,`id_item`),
  CONSTRAINT `ordered_item_id_order`
    FOREIGN KEY (`id_order`)
    REFERENCES `order_info` (`id`)
    ON UPDATE CASCADE,
  CONSTRAINT `ordered_item_id_item`
    FOREIGN KEY (`id_item`)
    REFERENCES `item` (`id`)
    ON UPDATE CASCADE)
ENGINE = InnoDB;

INSERT INTO `ordered_item` (`id_order`,`id_item`,`quantity`,`cost`) VALUES ('1e6ff61f-e059-410f-8410-3aa3cb116236',42,1,9.20);
INSERT INTO `ordered_item` (`id_order`,`id_item`,`quantity`,`cost`) VALUES ('1e6ff61f-e059-410f-8410-3aa3cb116236',6,2,500.0);
INSERT INTO `ordered_item` (`id_order`,`id_item`,`quantity`,`cost`) VALUES ('85ab8b69-cc90-4852-a29f-0f16e31dc1f4',2,1,4.33);
INSERT INTO `ordered_item` (`id_order`,`id_item`,`quantity`,`cost`) VALUES ('85ab8b69-cc90-4852-a29f-0f16e31dc1f4',11,1,4.00);
INSERT INTO `ordered_item` (`id_order`,`id_item`,`quantity`,`cost`) VALUES ('85ab8b69-cc90-4852-a29f-0f16e31dc1f4',42,1,12.2);
INSERT INTO `ordered_item` (`id_order`,`id_item`,`quantity`,`cost`) VALUES ('7457aa24-387b-456a-9e68-c7a8bfbc32bb',3,2,1);
INSERT INTO `ordered_item` (`id_order`,`id_item`,`quantity`,`cost`) VALUES ('7457aa24-387b-456a-9e68-c7a8bfbc32bb',9,3,1);
INSERT INTO `ordered_item` (`id_order`,`id_item`,`quantity`,`cost`) VALUES ('7457aa24-387b-456a-9e68-c7a8bfbc32bb',10,1,1);
INSERT INTO `ordered_item` (`id_order`,`id_item`,`quantity`,`cost`) VALUES ('7457aa24-387b-456a-9e68-c7a8bfbc32bb',1,1,1);
INSERT INTO `ordered_item` (`id_order`,`id_item`,`quantity`,`cost`) VALUES ('b9e97e78-972c-473c-a679-6f8d057adf61',4,2,1);
INSERT INTO `ordered_item` (`id_order`,`id_item`,`quantity`,`cost`) VALUES ('b9e97e78-972c-473c-a679-6f8d057adf61',5,1,1);
INSERT INTO `ordered_item` (`id_order`,`id_item`,`quantity`,`cost`) VALUES ('b9e97e78-972c-473c-a679-6f8d057adf61',42,1,1);

-- -----------------------------------------------------
-- Table `range`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `range` ;
CREATE TABLE IF NOT EXISTS `range` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `min` INT NOT NULL,
  `max` INT NOT NULL,
  `shipping_cost` FLOAT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

INSERT INTO `range` (`id`,`min`,`max`,`shipping_cost`) VALUES (1,0,2,6.1);
INSERT INTO `range` (`id`,`min`,`max`,`shipping_cost`) VALUES (2,3,5,5.2);
INSERT INTO `range` (`id`,`min`,`max`,`shipping_cost`) VALUES (3,6,30,2.3);
INSERT INTO `range` (`id`, `min`, `max`, `shipping_cost`) VALUES ('4', '5', '9.99', '2.99');


-- -----------------------------------------------------
-- Table `vendor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vendor` ;
CREATE TABLE IF NOT EXISTS `vendor` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `score` INT NOT NULL,
  `free_limit` FLOAT NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

INSERT INTO `vendor` (`id`,`name`,`score`,`free_limit`) VALUES (1,'SAMcommerce',5,24.99);
INSERT INTO `vendor` (`id`,`name`,`score`,`free_limit`) VALUES (2,'Mondadori',4,9.99);
INSERT INTO `vendor` (`id`,`name`,`score`,`free_limit`) VALUES (3,'Mercatino',3.5,49.99);
INSERT INTO `vendor` (`id`, `name`, `score`, `free_limit`) VALUES ('4', 'Rossopomodoro', '3', '15.20');
INSERT INTO `vendor` (`id`, `name`, `score`, `free_limit`) VALUES ('5', 'Spontini', '4', '10.50');
INSERT INTO `vendor` (`id`, `name`, `score`, `free_limit`) VALUES ('6', 'Grom', '5', '12.99');
INSERT INTO `vendor` (`id`, `name`, `score`, `free_limit`) VALUES ('7', 'Il Felice Gelato', '5', '5.99');
INSERT INTO `vendor` (`id`, `name`, `score`, `free_limit`) VALUES ('8', 'MediaWorld', '3', '50.55');
INSERT INTO `vendor` (`id`, `name`, `score`, `free_limit`) VALUES ('9', 'Sephora', '2', '20.65');


-- -----------------------------------------------------
-- Table `shipping_policy`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shipping_policy` ;
CREATE TABLE IF NOT EXISTS `shipping_policy` (
  `id_range` INT NOT NULL,
  `id_vendor` INT NOT NULL,
  PRIMARY KEY (`id_range`, `id_vendor`),
  CONSTRAINT `shipping_id_range`
    FOREIGN KEY (`id_range`)
    REFERENCES `range` (`id`)
    ON UPDATE CASCADE,
  CONSTRAINT `shipping_id_vendor`
    FOREIGN KEY (`id_vendor`)
    REFERENCES `vendor` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

INSERT INTO `shipping_policy` (`id_range`,`id_vendor`) VALUES (1,1);
INSERT INTO `shipping_policy` (`id_range`,`id_vendor`) VALUES (2,1);
INSERT INTO `shipping_policy` (`id_range`,`id_vendor`) VALUES (3,1);
INSERT INTO `shipping_policy` (`id_range`,`id_vendor`) VALUES (1,2);
INSERT INTO `shipping_policy` (`id_range`,`id_vendor`) VALUES (2,2);
INSERT INTO `shipping_policy` (`id_range`, `id_vendor`) VALUES ('1', '4');
INSERT INTO `shipping_policy` (`id_range`, `id_vendor`) VALUES ('2', '4');
INSERT INTO `shipping_policy` (`id_range`, `id_vendor`) VALUES ('1', '5');
INSERT INTO `shipping_policy` (`id_range`, `id_vendor`) VALUES ('2', '5');
INSERT INTO `shipping_policy` (`id_range`, `id_vendor`) VALUES ('3', '5');
INSERT INTO `shipping_policy` (`id_range`, `id_vendor`) VALUES ('4', '6');
INSERT INTO `shipping_policy` (`id_range`, `id_vendor`) VALUES ('4', '7');
INSERT INTO `shipping_policy` (`id_range`, `id_vendor`) VALUES ('3', '8');
INSERT INTO `shipping_policy` (`id_range`, `id_vendor`) VALUES ('1', '9');
INSERT INTO `shipping_policy` (`id_range`, `id_vendor`) VALUES ('2', '9');


-- -----------------------------------------------------
-- Table `view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `view` ;
CREATE TABLE IF NOT EXISTS `view` (
  `id_user` INT NOT NULL,
  `id_item` INT NOT NULL,
  `date` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id_user`, `id_item`,`date`),
  CONSTRAINT `view_id_item`
    FOREIGN KEY (`id_item`)
    REFERENCES `item` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `view_id_user`
    FOREIGN KEY (`id_user`)
    REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

INSERT INTO `view` (`id_user`,`id_item`,`date`) VALUES (4,3, timestamp("2021-01-26","18:32:11"));
INSERT INTO `view` (`id_user`,`id_item`,`date`) VALUES (4,6, timestamp("2021-01-26","18:42:11"));
INSERT INTO `view` (`id_user`,`id_item`,`date`) VALUES (4,42, timestamp("2021-03-30","19:50:11"));

-- -----------------------------------------------------
-- Table `price`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `price` ;
CREATE TABLE IF NOT EXISTS `price` (
  `id_vendor` INT NOT NULL,
  `id_item` INT NOT NULL,
  `price` FLOAT NOT NULL,
  PRIMARY KEY (`id_vendor`, `id_item`),
  CONSTRAINT `price_id_vendor`
    FOREIGN KEY (`id_vendor`)
    REFERENCES `vendor` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `price_id_item`
    FOREIGN KEY (`id_item`)
    REFERENCES `item` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (1,1, 20.99);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (1,2, 5.73);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (1,3, 2.90);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (1,4, 29.44);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (1,5, 62.21);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (1,6, 500.00);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (1,7, 2.50);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (1,8, 9.50);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (1,9, 4.5);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (1,10, 13.00);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (1,11, 5.00);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (1,42, 9.20);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (2,1, 24.99);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (2,2, 4.33);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (2,3, 2.39);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (2,4, 44.44);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (2,5, 82.21);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (2,6, 299.00);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (2,7, 1.20);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (2,8, 7.50);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (2,9, 4.5);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (2,10, 7.00);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (2,11, 4.00);
INSERT INTO `price` (`id_vendor`,`id_item`,`price`) VALUES (2,42, 12.20);
INSERT INTO `price` (`id_vendor`, `id_item`, `price`) VALUES ('6', '7', '6.66');
INSERT INTO `price` (`id_vendor`, `id_item`, `price`) VALUES ('7', '7', '12.99');
INSERT INTO `price` (`id_vendor`, `id_item`, `price`) VALUES ('4', '9', '8.00');
INSERT INTO `price` (`id_vendor`, `id_item`, `price`) VALUES ('5', '9', '4.50');
INSERT INTO `price` (`id_vendor`, `id_item`, `price`) VALUES ('8', '5', '120.99');
INSERT INTO `price` (`id_vendor`, `id_item`, `price`) VALUES ('8', '4', '7.99');
INSERT INTO `price` (`id_vendor`, `id_item`, `price`) VALUES ('2', '31', '10.19');
INSERT INTO `price` (`id_vendor`, `id_item`, `price`) VALUES ('2', '32', '28.98');



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;