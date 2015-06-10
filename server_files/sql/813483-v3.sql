-- phpMyAdmin SQL Dump
-- version 3.5.8
-- http://www.phpmyadmin.net
--
-- Хост: localhost
-- Время создания: Июн 10 2015 г., 05:04
-- Версия сервера: 5.5.32
-- Версия PHP: 5.4.17

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `813483`
--

-- --------------------------------------------------------

--
-- Структура таблицы `access`
--

CREATE TABLE IF NOT EXISTS `access` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `index` int(11) DEFAULT NULL,
  `info` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

--
-- Дамп данных таблицы `access`
--

INSERT INTO `access` (`id`, `index`, `info`) VALUES
(1, 0, 'Почта не подтверждена'),
(2, 1, 'Подтвержденный пользователь'),
(3, 2, 'Родитель'),
(4, 3, 'Член родительский комитета'),
(5, 4, 'Учитель'),
(6, 5, 'Классный руководитель'),
(7, 6, 'Администрация');

-- --------------------------------------------------------

--
-- Структура таблицы `categories`
--

CREATE TABLE IF NOT EXISTS `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` mediumtext COLLATE utf8_bin,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=35 ;

--
-- Дамп данных таблицы `categories`
--

INSERT INTO `categories` (`id`, `name`) VALUES
(1, 'Главное'),
(2, '1-а'),
(3, '1-б'),
(4, '1-в'),
(5, '2-а'),
(6, '2-б'),
(7, '2-в'),
(8, '3-а'),
(9, '3-б'),
(10, '3-в'),
(11, '4-а'),
(12, '4-б'),
(13, '4-в'),
(14, '5-а'),
(15, '5-б'),
(16, '5-в'),
(17, '6-а'),
(18, '6-б'),
(19, '6-в'),
(20, '7-а'),
(21, '7-б'),
(22, '7-в'),
(23, '8-а'),
(24, '8-б'),
(25, '8-в'),
(26, '9-а'),
(27, '9-б'),
(28, '9-в'),
(29, '10-а'),
(30, '10-б'),
(31, '10-в'),
(32, '11-а'),
(33, '11-б'),
(34, '11-в');

-- --------------------------------------------------------

--
-- Структура таблицы `class`
--

CREATE TABLE IF NOT EXISTS `class` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` mediumtext CHARACTER SET utf8,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='content all classes (digits and letters)' AUTO_INCREMENT=35 ;

--
-- Дамп данных таблицы `class`
--

INSERT INTO `class` (`id`, `name`) VALUES
(1, 'Не имеет класса'),
(2, '1-а'),
(3, '1-б'),
(4, '1-в'),
(5, '2-а'),
(6, '2-б'),
(7, '2-в'),
(8, '3-а'),
(9, '3-б'),
(10, '3-в'),
(11, '4-а'),
(12, '4-б'),
(13, '4-в'),
(14, '5-а'),
(15, '5-б'),
(16, '5-в'),
(17, '6-а'),
(18, '6-б'),
(19, '6-в'),
(20, '7-а'),
(21, '7-б'),
(22, '7-в'),
(23, '8-а'),
(24, '8-б'),
(25, '8-в'),
(26, '9-а'),
(27, '9-б'),
(28, '9-в'),
(29, '10-а'),
(30, '10-б'),
(31, '10-в'),
(32, '11-а'),
(33, '11-б'),
(34, '11-в');

-- --------------------------------------------------------

--
-- Структура таблицы `classes`
--

CREATE TABLE IF NOT EXISTS `classes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_class` int(11) DEFAULT NULL,
  `id_student` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_class` (`id_class`),
  KEY `id_student` (`id_student`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='store info about relationships between pupils and classes' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `gcm_users`
--

CREATE TABLE IF NOT EXISTS `gcm_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gcm_regid` text,
  `name` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `marks`
--

CREATE TABLE IF NOT EXISTS `marks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_subject` int(11) DEFAULT NULL,
  `id_student` int(11) DEFAULT NULL,
  `mark` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `info` mediumtext CHARACTER SET utf8,
  PRIMARY KEY (`id`),
  KEY `id_subject` (`id_subject`),
  KEY `id_student` (`id_student`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='this table is something about digital journal - store marks,' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `news`
--

CREATE TABLE IF NOT EXISTS `news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `news_head` mediumtext CHARACTER SET utf8,
  `news_text` mediumtext CHARACTER SET utf8,
  `who_add` int(11) NOT NULL DEFAULT '-1',
  `publishing_time` mediumtext CHARACTER SET utf8,
  `category` int(11) DEFAULT NULL,
  `important` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='main news table' AUTO_INCREMENT=26 ;

--
-- Дамп данных таблицы `news`
--

INSERT INTO `news` (`id`, `news_head`, `news_text`, `who_add`, `publishing_time`, `category`, `important`) VALUES
(3, 'Head of news #3', 'Some dummy text. Some dummy text. Some dummy text.', 1, '08/10/2014', 1, 0),
(4, 'Заголовок новости #4', 'Текст новости, Текст новости, Текст новости', 1, '06/10/2014', 1, 0),
(5, 'Родительское собрание\r\nв 5-А', 'Вниманию Родителей!\n\n5-А вечно:\n- сидят на сайте http://vk.com;\n- курят;\n- звонят по номеру 077809406;\n- пишут на почту gaga-ya@hotmail.com.\n\nПримите меры!', 1, '06/04/2015', 14, 1),
(7, 'Последний звонок', 'Уважаемые учащиеся и их родители!\n\n25/05/2015 (понедельник) в 8:30 состоится торжественная линейка по случаю "Последнего звонка" и окончанию учебного года.\n\nВ программе:\n- нудные песни;\n- плаксивые выпускницы;\n- народные пляски;\n- типа колокольчик;\n- поздравление директора купленными в последний момент "вениками".\n\nПо окончанию ваших детей ожидает знатный бухач в парке.\n\nЖелаем приятного дня. С уважением злой админ! :)', 1, '24/05/2015', 1, 1),
(21, 'Выпускной вечер', '24/06/2015 состоится выпускной вечер. \n\nПросьба сдать деньги на фото и видео съемку. Пусть ваши дети пересмотрят все упущенные из-за коньяка моменты! Не лишайте их выпуского вечера!\n\nРодительский коммитет 11-а класса. ', 1, '06/06/2015', 32, 0),
(22, 'Выпускной вечер', '24/06/2015 состоится выпускной вечер. \n\nПросьба сдать деньги на фото и видео съемку. Пусть ваши дети пересмотрят все упущенные из-за коньяка моменты! Не лишайте их выпуского вечера!\n\nРодительский коммитет 11-б класса. ', 1, '06/06/2015', 33, 0),
(25, 'Выпускной вечер', '24/06/2015 состоится выпускной вечер. \n\nПросьба сдать деньги на фото и видео съемку. Пусть ваши дети пересмотрят все упущенные из-за коньяка моменты! Не лишайте их выпуского вечера!\n\nРодительский коммитет 11-в класса. ', 1, '08/06/2015', 34, 0);

-- --------------------------------------------------------

--
-- Структура таблицы `student`
--

CREATE TABLE IF NOT EXISTS `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fio` mediumtext CHARACTER SET utf8,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='store info about all pupils in school' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `subject`
--

CREATE TABLE IF NOT EXISTS `subject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` mediumtext CHARACTER SET utf8,
  `classes` mediumtext CHARACTER SET utf8,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='store info about all subjets from school course and class di' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` mediumtext CHARACTER SET utf8,
  `password` mediumtext CHARACTER SET utf8,
  `fio` mediumtext CHARACTER SET utf8,
  `phone` mediumtext CHARACTER SET utf8,
  `access` int(11) DEFAULT '0',
  `class_id` int(11) DEFAULT '1',
  `reg_id` mediumtext CHARACTER SET utf8,
  `last_login` mediumtext CHARACTER SET utf8,
  PRIMARY KEY (`id`),
  KEY `id_class` (`class_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='users of application' AUTO_INCREMENT=3 ;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`id`, `email`, `password`, `fio`, `phone`, `access`, `class_id`, `reg_id`, `last_login`) VALUES
(1, 'gaga-ya@hotmail.com', '165b4004c413188d56fbddada760c038dd33e50d72b8b7c3c75f0ead94694fda', 'Гагауз Сергей', '077809406', 6, 14, NULL, '2015-06-10 09:54:05'),
(2, 'gaga-ya@mail.ru', 'e5d54226f603edb2ad3b9e4764b81becc0aa34a3f1be8d81642b304eca4db4e5', 'Гагауз Сергей', '077708956', 1, 1, NULL, '2015-01-13 11:57:21');

-- --------------------------------------------------------

--
-- Структура таблицы `users_temp`
--

CREATE TABLE IF NOT EXISTS `users_temp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `guid` int(11) DEFAULT NULL,
  `confirmation_code` mediumtext CHARACTER SET utf8,
  PRIMARY KEY (`id`),
  KEY `guid` (`guid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='waiting validation users' AUTO_INCREMENT=1 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
