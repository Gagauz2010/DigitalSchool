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
