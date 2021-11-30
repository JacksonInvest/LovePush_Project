-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 30, 2021 at 01:03 PM
-- Server version: 5.7.33-0ubuntu0.16.04.1
-- PHP Version: 7.1.33-34+ubuntu16.04.1+deb.sury.org+1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `lovepush`
--

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `image` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `capture_distance` int(11) NOT NULL DEFAULT '0' COMMENT 'NotUsed (in meters) distance range for other members profiles capture not used',
  `remember_token` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `verify_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'used for forgot password',
  `verify_code_created_at` datetime DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`id`, `name`, `email`, `phone`, `image`, `password`, `capture_distance`, `remember_token`, `verify_code`, `verify_code_created_at`, `created_at`, `updated_at`) VALUES
(1, 'admin', 'admin@love-push.com', '7814817171', '1577344951.jpg', '$2y$10$oALoe/ZsdJCnM91RPUYkFOS.2tP2X/vTdmuJ5xqusIras5gP3hcj.', 100, 'i2hxP5HH4dtf4OWc0wkxqlR79WA1abLZX4EHlNIulz6wgqFZrfKUHCBUzERH', '774846', '2018-10-04 19:04:55', '2018-06-27 05:06:26', '2019-12-26 01:52:31');

-- --------------------------------------------------------

--
-- Table structure for table `app_links`
--

CREATE TABLE `app_links` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `app_links`
--

INSERT INTO `app_links` (`id`, `name`, `value`, `created_at`, `updated_at`) VALUES
(1, 'android', 'https://bit.ly/LovePushGooglePlay', '2019-11-06 13:18:39', '2019-11-11 06:28:03'),
(2, 'ios', 'https://bit.ly/LovePushApple', '2019-11-06 13:18:39', '2019-11-11 06:27:43');

-- --------------------------------------------------------

--
-- Table structure for table `block_list`
--

CREATE TABLE `block_list` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `blocked_user_id` int(11) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `chat_requests`
--

CREATE TABLE `chat_requests` (
  `id` int(11) NOT NULL,
  `from_user_id` int(11) DEFAULT NULL,
  `to_user_id` int(11) DEFAULT NULL,
  `status` enum('S','A','R','') NOT NULL COMMENT 'S=Send, A=Accept, R=Reject request',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `chat_status`
--

CREATE TABLE `chat_status` (
  `id` int(11) NOT NULL,
  `match_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `isDeleted` enum('0','1') NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `chat_status`
--

INSERT INTO `chat_status` (`id`, `match_id`, `user_id`, `isDeleted`, `created_at`, `updated_at`) VALUES
(261, 1150, 1345, '0', '2021-11-01 17:12:46', '2021-11-01 17:12:46'),
(262, 1150, 1344, '0', '2021-11-01 17:12:46', '2021-11-01 17:12:46'),
(263, 1151, 1346, '0', '2021-11-06 22:52:56', '2021-11-06 22:52:56'),
(264, 1151, 1347, '0', '2021-11-06 22:52:56', '2021-11-06 22:52:56');

-- --------------------------------------------------------

--
-- Table structure for table `connect_requests`
--

CREATE TABLE `connect_requests` (
  `id` int(11) NOT NULL,
  `from_user_id` int(11) DEFAULT NULL,
  `to_user_id` int(11) DEFAULT NULL,
  `status` enum('S','A','R','') NOT NULL COMMENT 'S=Send, A=Accept, R=Reject request',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `connect_requests`
--

INSERT INTO `connect_requests` (`id`, `from_user_id`, `to_user_id`, `status`, `created_at`, `updated_at`) VALUES
(2558, 1346, 1344, 'S', '2021-11-02 07:43:28', '2021-11-02 07:43:28'),
(2559, 1347, 1344, 'S', '2021-11-06 17:22:00', '2021-11-06 17:22:00'),
(2560, 1347, 1345, 'S', '2021-11-06 17:22:15', '2021-11-06 17:22:15'),
(2561, 1346, 1345, 'S', '2021-11-06 17:30:46', '2021-11-06 17:30:46');

-- --------------------------------------------------------

--
-- Table structure for table `deleted_accounts`
--

CREATE TABLE `deleted_accounts` (
  `id` int(11) NOT NULL,
  `lang_id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone_code` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `reasons` text,
  `description` longtext,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `deleted_accounts`
--

INSERT INTO `deleted_accounts` (`id`, `lang_id`, `name`, `username`, `email`, `phone_code`, `phone`, `reasons`, `description`, `created_at`, `updated_at`) VALUES
(1, 0, 'shine', 'shine', 'shine@mailinator.com', '+1', '', 'Instrusive adds', 'eeff', '2019-12-23 16:40:25', '2019-12-23 16:40:25'),
(2, 0, 'Felix', 'Fuchs33', 'colognedog777@gmail.com', '+1', NULL, 'Other', 'Yo', '2021-04-13 02:22:10', '2021-04-13 02:22:10'),
(3, 0, 'felix', 'tedt420', 'felixbarth1000@yahoo.de', '+1', '', 'Other', 'Byyye', '2021-10-06 17:23:17', '2021-10-06 17:23:17'),
(4, 0, 'Jones', 'Jonny Cash', 'begood_johny@ymail.com', '+1', '', 'Other', 'test', '2021-10-18 23:37:04', '2021-10-18 23:37:04');

-- --------------------------------------------------------

--
-- Table structure for table `est_scores`
--

CREATE TABLE `est_scores` (
  `id` int(11) NOT NULL DEFAULT '0',
  `test_id_1` int(11) NOT NULL,
  `test_id_2` int(11) NOT NULL,
  `scores` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `faq`
--

CREATE TABLE `faq` (
  `id` int(11) NOT NULL,
  `language_id` int(11) NOT NULL,
  `ques_type` varchar(255) NOT NULL,
  `question` longtext NOT NULL,
  `answer` longtext NOT NULL,
  `status` enum('0','1') NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `faq`
--

INSERT INTO `faq` (`id`, `language_id`, `ques_type`, `question`, `answer`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 'General', 'What is Love Push?', 'We help you to connect quickly and easily with new people in your area or\r\naround the world.', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(2, 1, 'General', 'How can I use Love Push?', 'You can install the app from Android and iOS App Stores.', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(3, 1, 'General', 'Can Love Push be used anywhere in the world?', 'Yes, you can use the app worldwide!', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(4, 1, 'General', 'What is the minimum age to sign up?', 'The minimum age to sign up for LovePush is 18 years.', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(5, 1, 'Registration and first steps', 'Can I register without Facebook/Instagram/G+?', 'Yes, you can also sign in with your mobile number.', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(6, 1, 'Registration and first steps', 'How can I edit my profile?', 'You can edit your profile by clicking on your profile picture in the menu.', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(7, 1, 'Registration and first steps', 'Where do I adjust my search settings?', 'You can change your search settings in the menu under Settings ? Filter.', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(8, 1, 'Account', 'Where do I set the language?', 'Set your language by selecting "Language" in the settings.', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(9, 1, 'Account', 'Can I deactivate message requests?', 'Yes, if you only want to be contacted by matches deactivate message requests in the settings under "stop private messaging".', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(10, 1, 'Account', 'Can I deactivate my account temporarily?', 'Temporarily deactivate your account by selecting "Hide me from search" in the settings.', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(11, 1, 'Account', 'How do I delete my account?', 'Delete your account in the settings under "Delete Account".', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(12, 1, 'Account', 'How do I add images and how do I delete images of myself?', 'You can add and delete your pictures in the profile editing.', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(13, 1, 'Features LovePush', 'Profile & Posts', 'Create an interesting and real profile!\r\nYou can also post pictures in your profile - pictures say more than 1000 words!', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(14, 1, 'Features LovePush', 'Swipe', 'You can choose between "next", "connect" or "date" request.\r\nAfter you have matched, you can start chatting.', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(15, 1, 'Features LovePush', 'Radar', 'You have an overview of all people near you. Visit their profile and connect!', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(16, 1, 'Features LovePush', 'Love & Connect Ads', 'Publish Love or Connect ads in your chosen city and meet the right people in the right place.', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(17, 1, 'Features LovePush', 'Personal Matching', 'You get suggestions for people who fit your character and who have the same interests as you.', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(18, 1, 'Features LovePush', 'Around', 'Our integrated Google API allows you to explore the hottest bars, restaurants, festivals & concerts in your area and invite your contacts directly to them.', '1', '2019-10-07 16:12:41', '2019-10-07 16:12:41'),
(19, 1, 'Features LovePush', 'Live Streams', 'Start or join a live session in your area and watch, comment or join!', '1', '2019-10-07 16:12:41', '2019-10-07 16:22:23'),
(20, 1, 'Memberships', 'Free & V.I.P Pass', 'With our V.I.P. Pass you can use all our functions nearly unlimited and earn money with our affiliate program.\r\nFurther information can be found in the price information.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(21, 1, 'Memberships', 'Free Trial', 'For 3 days you can test the V.I.P. Pass and all features unlimited!', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(22, 1, 'Memberships', 'Love Points', 'Not another membership?\r\nWith the LovePoints you are completely free!\r\nGet as many Points as you want without recurring payments.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(23, 1, 'Memberships', 'Payment methods', 'The V.I.P. Pass can be purchased by PayPal, credit card and mobile phone number.\r\nYou can also secure LovePoints with Online Banking.\r\nThe membership fee is debited on the first day of each billing period.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(24, 1, 'Memberships', 'Renewal of membership', 'Your membership will be extended by one month at the end of each membership period.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(25, 1, 'Memberships', 'Cancellation of Membership', 'To cancel for the following month send your cancellation at least 5 days before your membership expires to termination@love-push.com or cancel directly in the app.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(26, 1, 'Memberships', 'Right of Withdrawal', 'Send us the right of withdrawal at the latest 14 days after conclusion of your membership to termination@love-push.com.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(27, 1, 'Affiliate', 'How do I become a Love Push Affiliate?', 'Agree to the Affiliate Terms & Conditions and owe an active V.I.P. Pass.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(28, 1, 'Affiliate', 'How do I get Team Partner?', 'All you must do is copy your referral code in the Affiliate Overview and send it to your friends.\r\nYou can also use it to advertise on social media.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(29, 1, 'Affiliate', 'From what date can I earn commissions?', 'You can use the time until the new year to build up your team.\r\nFrom January 2020 you will be able to earn your first commissions.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(30, 1, 'Affiliate', 'What is the compensation model?', 'You will be compensated on 5 levels, you can find the exact model in the Affiliate Information.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(31, 1, 'Affiliate', 'When do I have to request a withdrawal?', 'Until the 24th of each month in affiliate menu „Withdrawal “, if you have a minimum pay-out balance of 25$.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(32, 1, 'Affiliate', 'Where and when do I get my commissions paid?', 'If you have successfully applied for your pay-out you will get your commission paid to your PayPal account on the 1st of every month.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(33, 1, 'Affiliate', 'Do I have to take care of taxes myself?', 'Yes, Love Push pays you the commissions incl. Taxes to your PayPal account.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(34, 2, 'Allgemein', 'Was ist Love Push?', 'Mithilfe einzigartiger Funktionen helfen wir dir, schnell und einfach mit neuen Menschen in deiner Stadt oder auf der ganzen Welt in Kontakt zu treten!', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(35, 2, 'Allgemein', 'Wie kann ich Love Push nutzen?', 'Unsere App kannst du in den App Stores auf Android und iOS installieren.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(36, 2, 'Allgemein', 'Ist Love Push überall auf der Welt nutzbar?', 'Ja, du kannst die App weltweit nutzen!', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(37, 2, 'Allgemein', 'Was ist das Mindestalter, um sich anzumelden?', 'Das Mindestalter um sich bei LovePush anzumelden ist 18 Jahre.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(38, 2, 'Anmeldung und erste Schritte', 'Kann ich mich auch ohne Facebook/Instagram/G+ anmelden?', 'Ja, du kannst dich auch mit deiner', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(39, 2, 'Anmeldung und erste Schritte', 'Wie kann ich mein Profil bearbeiten?', 'Dein Profil bearbeitest du, indem du im Menü auf dein Profilbild klickst.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(40, 2, 'Anmeldung und erste Schritte', 'Wo stelle ich meine Sucheinstellungen ein?', 'Deine Sucheinstellungen stellst du im Menü unter Einstellungen ? Filter ein.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(41, 2, 'Account', 'Wo stelle ich die Sprache ein?', 'Stelle deine Sprache in den Einstellungen unter „Sprache“ ein.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(42, 2, 'Account', 'Kann ich Nachrichtenanfragen deaktivieren?', 'Ja, wenn du nur von Matches angeschrieben werden willst deaktiviere Nachrichtenanfragen in den Einstellungen unter „stop private messaging“.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(43, 2, 'Account', 'Kann ich meinen Account vorrübergehend deaktivieren?', 'Deaktiviere deinen Account vorrübergehend, indem du in den Einstellungen unter „Hide me from search“.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(44, 2, 'Account', 'Wie lösche ich meinen Account?', 'Lösche deinen Account in den Einstellungen unter „Delete Account“.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(45, 2, 'Account', 'Wie füge ich Bilder hinzu und wie lösche ich Bilder von mir?', 'Deine Bilder kannst du in der Profilbearbeitung hinzufügen und löschen.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(46, 2, 'Features LovePush', 'Profil & Posts', 'Gestalte ein interessantes und echtes Profil!\r\nZudem kannst du Bilder in deinem Profil posten - Bilder sagen mehr als 1000 Worte!', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(47, 2, 'Features LovePush', 'Swipe', 'Du kannst zwischen "next", "connect" oder "date" Request entscheiden. \r\nNachdem du gematcht hast, kannst du mit dem Chatten beginnen.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(48, 2, 'Features LovePush', 'Radar', 'Du hast einen Überblick über alle Personen in deiner Nähe. Besuche ihr Profil und connecte dich!', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(49, 2, 'Features LovePush', 'Love & Connect Ads', 'Veröffentliche Love- oder Connect-Anzeigen in deiner ausgewählten Stadt und lerne die richtigen Personen am richtigen Ort kennen.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(50, 2, 'Features LovePush', 'Personal Matching', 'Du erhältst Vorschläge für Menschen, die zu deinem Charakter passen und welche die gleichen Interessen haben wie du.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(51, 2, 'Features LovePush', 'Around', 'Durch die von uns integrierte Google API hast du die Möglichkeit, die angesagtesten Bars, Restaurants, Festivals & Konzerte in deiner Umgebung zu erkunden und deine Kontakte direkt zu ihnen einzuladen.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(52, 2, 'Features LovePush', 'Live Streams', 'Starte oder nehme an einer Live-Session von Leuten in deiner Umgebung teil und schau zu, kommentiere oder nimm daran teil!', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(53, 2, 'Mitgliedschaften', 'Free & V.I.P Pass', 'Mit unserem V.I.P. Pass kannst du alle unsere Funktionen unbegrenzt nutzen und zudem mit unserem Partnerprogramm Geld verdienen. \r\nWeitere Informationen findest du in den Preisinformationen.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(54, 2, 'Mitgliedschaften', 'Free Trial', '3 Tage kannst du den V.I.P. Pass und somit alle Features unbegrenzt testen!', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(55, 2, 'Mitgliedschaften', 'Love Points', 'Nicht schon wieder eine Mitgliedschaft?\r\nMit den LovePoints bist du völlig ungebunden!\r\nErhalte so viele Points, wie du kaufst ohne wiederkehrende Zahlungen.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(56, 2, 'Mitgliedschaften', 'Zahlungsmöglichkeiten', 'Den V.I.P. Pass kannst du per PayPal, Kreditkarte und mit deiner Handynummer erwerben.\r\nLovePoints kannst du dir zudem noch mit Sofortüberweisung sichern.\r\nDie Mitgliedsgebühr wird jeweils am ersten Tag eines Abrechnungszeitraums abgebucht.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(57, 2, 'Mitgliedschaften', 'Verlängerung der Mitgliedschaft', 'Deine Mitgliedschaft verlängert sich jeweils zum Ende deines Mitgliedsstarts um einen Monat.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(58, 2, 'Mitgliedschaften', 'Kündigung der Mitgliedschaft', 'Um für den Folgemonat zu kündigen sende die Kündigung bis spätestens 5 Tage vor Ablauf deiner Mitgliedschaft an termination@love-push.com oder kündige direkt in der App.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(59, 2, 'Mitgliedschaften', 'Widerrufsrecht', 'Sende uns das Widerrufsrecht bis spätestens 14 Tagen nach Abschluss deiner Mitgliedschaft an termination@love-push.com .', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(60, 2, 'Affiliate', 'Wie werde ich Love Affiliate?', 'Alles, was du tun musst, ist, die Affiliate AGB\'s zu akzeptieren und einen aktiven V.I.P. Pass zu besitzen.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(61, 2, 'Affiliate', 'Wie bekomme ich Teampartner?', 'Kopiere deinen Empfehlungscode in der Affiliate Overview und sende ihn an deine Freunde.\r\nDu kannst damit auch auf Social Media werben.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(62, 2, 'Affiliate', 'Ab welchem Datum kann ich Provisionen verdienen?', 'Du kannst die Zeit bis zum neuen Jahr nutzen, um dein Team aufzubauen.\r\nAb Januar 2020 kannst du die ersten Provisionen verdienen.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(63, 2, 'Affiliate', 'Wie ist das Vergütungsmodell?', 'Du wirst auf 5 Ebenen vergütet, das genaue Modell finden Sie in den Partnerinformationen.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(64, 2, 'Affiliate', 'Bis wann muss eine Auszahlung beantragt werden?', 'Bis zum 24. eines jeden Monats im Affiliate Menü unter „Withdrawal“, wenn Sie ein Mindestauszahlungssaldo von 25$ haben.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(65, 2, 'Affiliate', 'Wo und wann bekomme ich meine Provisionen ausgezahlt?', 'Wenn Sie Ihre Auszahlung erfolgreich beantragt haben, erhalten Sie Ihre Provision am 1. eines jeden Monats auf Ihr PayPal-Konto ausgezahlt.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05'),
(66, 2, 'Affiliate', 'Muss ich mich selbst um die Steuern kümmern?', 'Ja, Love Push zahlt Ihnen die Provisionen inkl. Steuern auf Ihr PayPal-Konto.', '1', '2019-10-07 16:12:41', '2019-10-07 16:23:05');

-- --------------------------------------------------------

--
-- Table structure for table `intentions`
--

CREATE TABLE `intentions` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1=active, 0=inactive',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `intentions`
--

INSERT INTO `intentions` (`id`, `name`, `status`, `created_at`, `updated_at`) VALUES
(1, 'relationship', 1, '2019-05-02 14:50:32', '2019-05-02 14:50:32'),
(2, 'friendship', 1, '2019-05-02 14:50:32', '2019-05-02 14:50:32'),
(3, 'daily activities', 1, '2019-05-02 14:50:55', '2019-05-02 14:50:55'),
(4, 'no expectations', 1, '2019-05-02 14:50:55', '2019-05-24 18:13:54');

-- --------------------------------------------------------

--
-- Table structure for table `likes`
--

CREATE TABLE `likes` (
  `id` int(11) NOT NULL,
  `from_user_id` int(11) NOT NULL,
  `to_user_id` int(11) NOT NULL,
  `status` tinyint(1) DEFAULT NULL COMMENT '1=Like,0=Dislike',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `likes`
--

INSERT INTO `likes` (`id`, `from_user_id`, `to_user_id`, `status`, `created_at`, `updated_at`) VALUES
(1303, 1344, 1345, 1, '2021-11-01 11:41:37', '2021-11-01 11:41:37'),
(1304, 1345, 1344, 1, '2021-11-01 11:42:17', '2021-11-01 11:42:17'),
(1305, 1347, 1346, 1, '2021-11-06 17:21:54', '2021-11-06 17:21:54'),
(1306, 1346, 1347, 1, '2021-11-06 17:22:18', '2021-11-06 17:22:18');

-- --------------------------------------------------------

--
-- Table structure for table `matches`
--

CREATE TABLE `matches` (
  `id` int(11) NOT NULL,
  `event_type` enum('L','C','M') NOT NULL COMMENT 'match found by L=Like, C=Connect req, M=by chat messaging req',
  `user_id` int(11) DEFAULT NULL COMMENT 'second like user_id',
  `like_by_me` int(11) DEFAULT NULL COMMENT 'first like user_id',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1=match, 0=unmatch',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `matches`
--

INSERT INTO `matches` (`id`, `event_type`, `user_id`, `like_by_me`, `status`, `created_at`, `updated_at`) VALUES
(1150, 'L', 1345, 1344, 1, '2021-11-01 11:42:17', '2021-11-01 11:42:17'),
(1151, 'L', 1346, 1347, 1, '2021-11-06 17:22:19', '2021-11-06 17:22:19');

-- --------------------------------------------------------

--
-- Table structure for table `migrations`
--

CREATE TABLE `migrations` (
  `id` int(10) UNSIGNED NOT NULL,
  `migration` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `batch` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `migrations`
--

INSERT INTO `migrations` (`id`, `migration`, `batch`) VALUES
(26, '2014_10_12_000000_create_users_table', 1),
(27, '2014_10_12_100000_create_password_resets_table', 1),
(28, '2016_06_01_000001_create_oauth_auth_codes_table', 1),
(29, '2016_06_01_000002_create_oauth_access_tokens_table', 1),
(30, '2016_06_01_000003_create_oauth_refresh_tokens_table', 1),
(31, '2016_06_01_000004_create_oauth_clients_table', 1),
(32, '2016_06_01_000005_create_oauth_personal_access_clients_table', 1),
(33, '2019_01_15_133005_create_post_ads_table', 1),
(34, '2019_01_16_074723_add_image_to_users_table', 1),
(35, '2019_01_16_075955_create_user_profile_images_table', 1),
(36, '2019_01_16_125501_add_role_to_users_table', 1),
(37, '2019_01_17_070145_add_city_country_to_users_table', 1),
(38, '2019_01_18_102128_create_user_requests_table', 1),
(39, '2019_01_18_103857_create_notifications_table', 1),
(40, '2019_01_21_072426_create_personality_questions_table', 2),
(41, '2019_01_21_072803_create_personality_questions_options_table', 2),
(42, '2019_01_21_095504_create_personality_tests_table', 2),
(43, '2019_01_22_042411_change_radius_to_post_ads_table', 3),
(44, '2019_01_23_093446_add_otp_to_users_table', 4),
(45, '2019_01_24_101128_create_feeds_table', 5),
(46, '2019_01_25_091432_add_seetingsparam_to_users_table', 6);

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `id` int(11) NOT NULL,
  `event_type` enum('L','S_C_R','A_C_R','R_C_R','S_M_R','A_M_R','R_M_R','S_L_R','A_L_R','P_L_R') NOT NULL COMMENT 'L=like, S_C_R=send connect request, A_C_R=accept connect request, R_C_R=reject connect request, S_M_R=send messaging request, A_M_R=accept messaging request, R_M_R=reject messaging request',
  `event_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `to_user_id` int(11) DEFAULT NULL,
  `status` smallint(1) NOT NULL DEFAULT '1' COMMENT '1=active, 0=inactive',
  `isRead` int(11) NOT NULL DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `notifications`
--

INSERT INTO `notifications` (`id`, `event_type`, `event_id`, `user_id`, `to_user_id`, `status`, `isRead`, `created_at`, `updated_at`) VALUES
(8085, 'S_L_R', 1303, 1344, 1345, 0, 1, '2021-11-01 11:41:37', '2021-11-01 11:42:17'),
(8086, 'A_L_R', 1304, 1345, 1344, 1, 1, '2021-11-01 11:42:17', '2021-11-01 12:39:56'),
(8087, 'S_C_R', 2558, 1346, 1344, 1, 0, '2021-11-02 07:43:28', '2021-11-02 07:43:28'),
(8088, 'S_L_R', 1305, 1347, 1346, 0, 1, '2021-11-06 17:21:55', '2021-11-06 17:22:20'),
(8089, 'S_C_R', 2559, 1347, 1344, 1, 0, '2021-11-06 17:22:00', '2021-11-06 17:22:00'),
(8090, 'S_C_R', 2560, 1347, 1345, 1, 0, '2021-11-06 17:22:15', '2021-11-06 17:22:15'),
(8091, 'A_L_R', 1306, 1346, 1347, 1, 1, '2021-11-06 17:22:20', '2021-11-06 17:31:11'),
(8092, 'S_C_R', 2561, 1346, 1345, 1, 0, '2021-11-06 17:30:46', '2021-11-06 17:30:46'),
(8093, 'P_L_R', 20, 1347, 1346, 1, 1, '2021-11-06 17:37:09', '2021-11-06 17:54:41'),
(8094, 'P_L_R', 21, 1346, 1347, 1, 0, '2021-11-06 17:51:54', '2021-11-06 17:51:54');

-- --------------------------------------------------------

--
-- Table structure for table `notification_descs`
--

CREATE TABLE `notification_descs` (
  `notification_id` int(11) NOT NULL,
  `language_id` tinyint(4) NOT NULL DEFAULT '1',
  `message` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `notification_descs`
--

INSERT INTO `notification_descs` (`notification_id`, `language_id`, `message`, `created_at`, `updated_at`) VALUES
(8085, 1, 'Clark Joy has sent you Date Request.', '2021-11-01 11:41:38', '2021-11-01 11:41:38'),
(8085, 2, 'Clark Joy hat Ihnen eine Terminanfrage gesendet.', '2021-11-01 11:41:38', '2021-11-01 11:41:38'),
(8086, 1, 'Smith Joy has accepted your Date Request.', '2021-11-01 11:42:17', '2021-11-01 11:42:17'),
(8086, 2, 'Smith Joy hat Ihre Terminanfrage angenommen.', '2021-11-01 11:42:17', '2021-11-01 11:42:17'),
(8087, 1, 'ChatnGift has sent you Connect Request', '2021-11-02 07:43:28', '2021-11-02 07:43:28'),
(8087, 2, 'ChatnGift hat Ihnen eine Verbindungsanforderung gesendet.', '2021-11-02 07:43:28', '2021-11-02 07:43:28'),
(8088, 1, 'Felix has sent you Date Request.', '2021-11-06 17:21:55', '2021-11-06 17:21:55'),
(8088, 2, 'Felix hat Ihnen eine Terminanfrage gesendet.', '2021-11-06 17:21:55', '2021-11-06 17:21:55'),
(8089, 1, 'Felix has sent you Connect Request', '2021-11-06 17:22:00', '2021-11-06 17:22:00'),
(8089, 2, 'Felix hat Ihnen eine Verbindungsanforderung gesendet.', '2021-11-06 17:22:00', '2021-11-06 17:22:00'),
(8090, 1, 'Felix has sent you Connect Request', '2021-11-06 17:22:15', '2021-11-06 17:22:15'),
(8090, 2, 'Felix hat Ihnen eine Verbindungsanforderung gesendet.', '2021-11-06 17:22:15', '2021-11-06 17:22:15'),
(8091, 1, 'ChatnGift has accepted your Date Request.', '2021-11-06 17:22:20', '2021-11-06 17:22:20'),
(8091, 2, 'ChatnGift hat Ihre Terminanfrage angenommen.', '2021-11-06 17:22:20', '2021-11-06 17:22:20'),
(8092, 1, 'ChatnGift has sent you Connect Request', '2021-11-06 17:30:46', '2021-11-06 17:30:46'),
(8092, 2, 'ChatnGift hat Ihnen eine Verbindungsanforderung gesendet.', '2021-11-06 17:30:46', '2021-11-06 17:30:46'),
(8093, 1, 'Felix has liked your post.', '2021-11-06 17:37:09', '2021-11-06 17:37:09'),
(8093, 2, 'Felix hat Ihren Beitrag gefallen.', '2021-11-06 17:37:09', '2021-11-06 17:37:09'),
(8094, 1, 'ChatnGift has liked your post.', '2021-11-06 17:51:55', '2021-11-06 17:51:55'),
(8094, 2, 'ChatnGift hat Ihren Beitrag gefallen.', '2021-11-06 17:51:55', '2021-11-06 17:51:55');

-- --------------------------------------------------------

--
-- Table structure for table `oauth_access_tokens`
--

CREATE TABLE `oauth_access_tokens` (
  `id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `client_id` int(10) UNSIGNED NOT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `scopes` text COLLATE utf8mb4_unicode_ci,
  `revoked` tinyint(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `expires_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `oauth_access_tokens`
--

INSERT INTO `oauth_access_tokens` (`id`, `user_id`, `client_id`, `name`, `scopes`, `revoked`, `created_at`, `updated_at`, `expires_at`) VALUES
('001fcbe57f3a5a909724216113db567a8a7960b2916d24793869809a77e8640de024cb0031a99f1a', 1229, 1, 'user', '[]', 1, '2021-04-12 06:39:11', '2021-04-12 06:39:11', '2022-04-12 12:09:11'),
('0036cc9bdbc542411ab8748b5ebe01cb9ff1fae6929a32ddb5727d6069e4fd11ce032e8e7d9bdf82', 1343, 1, 'user', '[]', 1, '2021-10-28 06:29:19', '2021-10-28 06:29:19', '2022-10-28 11:59:19'),
('00415656da3b9bc1ca91e8bc5336618a9023e5810808ed2610311cf59f7c606a3932cc99e85abba3', 1259, 1, 'user', '[]', 0, '2021-04-08 23:28:27', '2021-04-08 23:28:27', '2022-04-09 04:58:27'),
('00b42d09cc9592be395d66611eaa32ab727706a7415a149b20f2835c93337312967f0e641d3fd428', 1230, 1, 'user', '[]', 0, '2021-04-05 23:55:09', '2021-04-05 23:55:09', '2022-04-06 05:25:09'),
('011e613b18b23d1fdc67f4468476d3c53cf1b921e53602a4e291fda2b8156005cc1ee7fd40e964e5', 1346, 1, 'user', '[]', 0, '2021-11-02 01:10:16', '2021-11-02 01:10:16', '2022-11-02 06:40:16'),
('01314b4cfd32ffc9e366b889a56f19514827c6b03b60bfdf693e3da77d83429fe02750a939b03ea3', 1290, 1, 'user', '[]', 0, '2021-04-12 15:26:28', '2021-04-12 15:26:28', '2022-04-12 20:56:28'),
('01918f57148ac0fc9a0416a211f1292314a9128f06fe34b80d6e986ddfc2b69ac9818a1020e2fd26', 1221, 1, 'user', '[]', 0, '2021-03-31 01:36:53', '2021-03-31 01:36:53', '2022-03-31 07:06:53'),
('01ca22e29c0a919a3bb52a0d67055aa2825440d2394aaa63324d1cc3ed823935b3fb3f4c55e62f0a', 1324, 1, 'user', '[]', 0, '2021-04-21 07:10:50', '2021-04-21 07:10:50', '2022-04-21 12:40:50'),
('040a64b4821a33c15e0fba2570eb471808c8604353609866c2e59dbfd10bd8b108fb25edc3821ed9', 1229, 1, 'user', '[]', 1, '2021-04-13 06:04:09', '2021-04-13 06:04:09', '2022-04-13 11:34:09'),
('042ecc8fb4ce2bd4470b8dbb6d6fc1f6077e941a07e8fd828f5044694d1d6fcfb05eb65b9b56f5e1', 1295, 1, 'user', '[]', 0, '2021-04-12 22:45:12', '2021-04-12 22:45:12', '2022-04-13 04:15:12'),
('04a06f9e087e431be079a0b8f846c65ecd2513b832bdf8fc8d3018c3a4af5456588b3b1dee06f012', 1252, 1, 'user', '[]', 1, '2021-04-08 05:45:36', '2021-04-08 05:45:36', '2022-04-08 11:15:36'),
('05ad0225a06d9bbbf3e6ae0a6ab26b1eefddf4f95c8842976b7f72245b08ddbd587df724705faca7', 1257, 1, 'user', '[]', 1, '2021-04-08 02:34:39', '2021-04-08 02:34:39', '2022-04-08 08:04:39'),
('0614d4876ea3861c7293234b74ea51fa7ec692de917283b873e7367215671f1948a916f0a3202326', 1245, 1, 'user', '[]', 0, '2021-04-07 05:31:11', '2021-04-07 05:31:11', '2022-04-07 11:01:11'),
('072bc73c1145bf31446ae3b2785a4b8cd42164910471f8e8aaa071523b08e4444ab565d618a96e51', 1333, 1, 'user', '[]', 0, '2021-10-21 06:03:59', '2021-10-21 06:03:59', '2022-10-21 11:33:59'),
('07bc4543ef8fade8210195a16359b8b653528368a18a72df634f90bdd736b5d45f947087b28e4711', 1337, 1, 'user', '[]', 0, '2021-10-26 12:32:18', '2021-10-26 12:32:18', '2022-10-26 18:02:18'),
('07f4e5c39233503dd1e31c9630466eb208e972f4ed2086d74c946d0787553f043562ff83aea3a878', 1335, 1, 'user', '[]', 0, '2021-10-26 05:41:01', '2021-10-26 05:41:01', '2022-10-26 11:11:01'),
('089b15066664bd5a2bb4bc3a4ec487f665a2b4d3a7febda1e5c0cb807a5849ae809936a702718531', 1333, 1, 'user', '[]', 0, '2021-10-21 05:36:48', '2021-10-21 05:36:48', '2022-10-21 11:06:48'),
('0988c62b77fd599835a367e67e5340a0d3ac7bd6666ae256facf53feca3bff4746d55f88557a7f11', 1344, 1, 'user', '[]', 0, '2021-11-01 05:59:22', '2021-11-01 05:59:22', '2022-11-01 11:29:22'),
('0c1bfd66cf0f03daa293a7205926e3aeb3036691b2bac780d7f371db098853957dcea3d4b406b4ba', 1326, 1, 'user', '[]', 1, '2021-10-07 01:23:01', '2021-10-07 01:23:01', '2022-10-07 06:53:01'),
('0c21017564f170fde834c4d0ae0f16609c1158cb5f2a3fe6f8f6b666d88e18669ff531c0fcfab0ca', 1213, 1, 'user', '[]', 0, '2021-01-12 00:29:30', '2021-01-12 00:29:30', '2022-01-12 05:59:30'),
('0c9002b9718b9cd80b1ecc593569d88a2ce8ad556781bc9802dcec197198a739c49f623053cb67d6', 1196, 1, 'user', '[]', 1, '2020-02-07 04:07:25', '2020-02-07 04:07:25', '2021-02-07 09:37:25'),
('0d19b00e81392429a39cbb92d53273c282529587036fdeb63af232bda161ef33d3f1b4d1798967a9', 1345, 1, 'user', '[]', 0, '2021-11-01 06:06:35', '2021-11-01 06:06:35', '2022-11-01 11:36:35'),
('0e31bf6de89b408c108bcdf208ab278587ff6a7ed96ff06d34ce8abcc4c632f0708cf723d6261439', 1221, 1, 'user', '[]', 0, '2021-03-31 01:32:13', '2021-03-31 01:32:13', '2022-03-31 07:02:13'),
('0e8e6f3513dfd846eb07e6ce7b1c1105d8d24c7f9cda9cd425f8a426dc5f1d30f25283b4466fc667', 1279, 1, 'user', '[]', 1, '2021-04-14 02:23:11', '2021-04-14 02:23:11', '2022-04-14 07:53:11'),
('0f56eb01674a3299ff0f4cfe776c213ea0f2aedc1d5da43e041fc38806adbfde2c4cab126ba03073', 1223, 1, 'user', '[]', 1, '2021-04-02 01:52:11', '2021-04-02 01:52:11', '2022-04-02 07:22:11'),
('0f7976b400ec19803c6e35a550cdf62c00f03de48e0254e841327fc2cd7eea113513a7a270181c9b', 1333, 1, 'user', '[]', 0, '2021-10-21 05:08:21', '2021-10-21 05:08:21', '2022-10-21 10:38:21'),
('0fe64628016acbb13027bd09a093df1ab3e5004ccb29ca2e3052de4dfde32f01b36af2bee1a819e1', 1331, 1, 'user', '[]', 0, '2021-10-25 05:10:12', '2021-10-25 05:10:12', '2022-10-25 10:40:12'),
('102b0d6fd3122a3baa7382793728f84c196b820a0d53cf124aff98a9e4997dbc90a71903c077a4d7', 1337, 1, 'user', '[]', 1, '2021-10-26 12:29:10', '2021-10-26 12:29:10', '2022-10-26 17:59:10'),
('1086791d6092c39b9eb76fd4623b80be8f9a4778347f642c72c55a1ca487fda0a03d39500030c74f', 1223, 1, 'user', '[]', 1, '2021-04-07 06:12:11', '2021-04-07 06:12:11', '2022-04-07 11:42:11'),
('1382d3a7c65656ca4074da89785960a50514b81a2d9b6295213c7725920f95d2f538e5b743ab5d46', 1196, 1, 'user', '[]', 0, '2020-02-07 04:06:42', '2020-02-07 04:06:42', '2021-02-07 09:36:42'),
('14131dbea39d55a47641a08fbc7002f187bf85fafdc33960f7beddf3c3e8bd543ecbfe21e0dd7fd0', 1304, 1, 'user', '[]', 0, '2021-04-13 02:14:25', '2021-04-13 02:14:25', '2022-04-13 07:44:25'),
('14229d69de3fc8d45c600c854dc51f0e8d05293321449c71664a870884c5d8c0b2e2ecc9e8c3d30d', 1290, 1, 'user', '[]', 0, '2021-04-20 06:01:09', '2021-04-20 06:01:09', '2022-04-20 11:31:09'),
('148579de00db60d22ca76c5c84678a3feef57e6e37f0ce58bbdb2c443eee7f49478be7c0dc2f724c', 1290, 1, 'user', '[]', 1, '2021-04-21 04:48:19', '2021-04-21 04:48:19', '2022-04-21 10:18:19'),
('14b45f50598f63f7248bfc0ed6d50c427d47348e64aa9fff126215f4d46f8225e26c304539bc158b', 1193, 1, 'user', '[]', 0, '2021-04-06 03:56:54', '2021-04-06 03:56:54', '2022-04-06 09:26:54'),
('157f8be5cc53d8b8cd752d9814e44ded4b7072d8271072227a5aa9fa3ada159fc56dc45949977b7d', 1329, 1, 'user', '[]', 1, '2021-10-20 06:39:41', '2021-10-20 06:39:41', '2022-10-20 12:09:41'),
('15a2a3947b96f5843cfba303f8c9aef1cfc4036ac7aecff81886a243f1b4bce21af92faf6603ea42', 1331, 1, 'user', '[]', 1, '2021-10-25 05:11:03', '2021-10-25 05:11:03', '2022-10-25 10:41:03'),
('1629de20a5f6d9d1efe6f0db612d550d68500112366c9c6c64cb8936a91e0522fe602ba01fdf1183', 1274, 1, 'user', '[]', 0, '2021-04-09 05:13:36', '2021-04-09 05:13:36', '2022-04-09 10:43:36'),
('170475bde367ee0db90a0e67def36d9f27fea54c2882723f0c875108847e0b5ea6f6da516fb09e8f', 1269, 1, 'user', '[]', 1, '2021-04-09 03:50:46', '2021-04-09 03:50:46', '2022-04-09 09:20:46'),
('1747becbdd8ffce6649ac2a5e499eb89c48084074381d90bcf61a05d3d917a0f192c05824b3fed60', 1284, 1, 'user', '[]', 0, '2021-04-12 08:25:43', '2021-04-12 08:25:43', '2022-04-12 13:55:43'),
('1856fa4a77d44001962b160a5ad2dd4749f52b13171aeaab4b63907108e937d491de5b8b2cac7198', 1222, 1, 'user', '[]', 0, '2021-04-01 08:02:09', '2021-04-01 08:02:09', '2022-04-01 13:32:09'),
('18637f24b46e43baf084702ef55125ed8ac11fadb64ee76ef1a280964bf7c1ceb83970882a240a49', 1205, 1, 'user', '[]', 0, '2021-03-18 08:42:12', '2021-03-18 08:42:12', '2022-03-18 14:12:12'),
('186e5f99f608874b106d317aff0401e02495e1d275d2f2b74e4d60b66a410b07473f4dcf98fa264d', 1333, 1, 'user', '[]', 0, '2021-10-21 06:24:24', '2021-10-21 06:24:24', '2022-10-21 11:54:24'),
('186ee3b6a8b3a28e96289cac78d45d343311ec2063f1a6166f93601e27b33ac477bf4081cf106f65', 1316, 1, 'user', '[]', 1, '2021-04-14 05:03:57', '2021-04-14 05:03:57', '2022-04-14 10:33:57'),
('193c67d8ed84b3e0b3a87bf26dfa6d0d53d61157fb1c9c647fbfb8b775371228aad61d9100addb48', 1333, 1, 'user', '[]', 0, '2021-10-21 06:38:03', '2021-10-21 06:38:03', '2022-10-21 12:08:03'),
('19c06c45bdfe97298ba316320bfa79863cfe2b376b6a35e2c4d84df025c3ac28633cc6562422fee2', 1284, 1, 'user', '[]', 1, '2021-04-12 08:27:24', '2021-04-12 08:27:24', '2022-04-12 13:57:24'),
('19e64bab43984286113eff57b0863bb0784991899a34ba21e4da698946e96c407c5aabc869ff0efe', 1307, 1, 'user', '[]', 0, '2021-04-13 03:05:42', '2021-04-13 03:05:42', '2022-04-13 08:35:42'),
('1a6a6ca1d4d6a09d6fa149b562f7d746129c0ba969e5db608fe815ff41c47c9471b9eb3050e089a9', 1276, 1, 'user', '[]', 0, '2021-04-09 05:43:20', '2021-04-09 05:43:20', '2022-04-09 11:13:20'),
('1adb0cd5f962626732f6e2d1ec3ea2ac4dec3892655e74e2b424d0e729cac73d32ba8d57f3b67efa', 1327, 1, 'user', '[]', 0, '2021-10-07 01:52:32', '2021-10-07 01:52:32', '2022-10-07 07:22:32'),
('1b0f4982faa6eca04c6b967b9cfea225554f5f0aaaf4256263800af47518e6e99cc6a618ad2d2802', 1333, 1, 'user', '[]', 0, '2021-10-21 06:15:54', '2021-10-21 06:15:54', '2022-10-21 11:45:54'),
('1b2fd87597ebe8e0c22b895b3e26ec80c118321b86f5d86ad0211d011bb77f512a02c19db1c1aef6', 1268, 1, 'user', '[]', 0, '2021-04-09 03:38:14', '2021-04-09 03:38:14', '2022-04-09 09:08:14'),
('1b301de8ec6155ca4afded139b973c5e14ceaa5b9608f0957960badc6d3c212a4b4f027fa076676b', 1270, 1, 'user', '[]', 0, '2021-04-09 04:03:23', '2021-04-09 04:03:23', '2022-04-09 09:33:23'),
('1beb9a7fedd0f6ff4ac5079a3e35e7baf411dae8ab304bdac340e460aa7d339561c9c4d8ee857d00', 1321, 1, 'user', '[]', 0, '2021-04-19 00:09:28', '2021-04-19 00:09:28', '2022-04-19 05:39:28'),
('1d189234468ed731414ee95e2a6f108a3fd16054f96c79252d7676c3462a02fe5b9568a79e12973a', 1223, 1, 'user', '[]', 1, '2021-04-07 06:48:41', '2021-04-07 06:48:41', '2022-04-07 12:18:41'),
('1d480123880d9f6aa0ea1cbb8359f6ed1ea5cdf280f5600ab0be3103ed5935d9457c3e7e39339346', 1193, 1, 'user', '[]', 0, '2020-02-07 00:49:24', '2020-02-07 00:49:24', '2021-02-07 06:19:24'),
('1f05086bd5e7c424c28dcc51f1b0ad307a1fec7fb68de6f593e7657abbe46721cc3a256c404e86aa', 1332, 1, 'user', '[]', 1, '2021-10-25 05:22:54', '2021-10-25 05:22:54', '2022-10-25 10:52:54'),
('1f72f91163b0ba2f4d107a3f7af90f04d74b7b30e0fdc5daff4a5d4e60d846e4f49f53ca347ecd02', 1300, 1, 'user', '[]', 0, '2021-04-12 23:44:33', '2021-04-12 23:44:33', '2022-04-13 05:14:33'),
('1f91f1be33453f1b37fc688ff047728d71af15bcbcd144cbada5234b7ad84b86d6ae6fc1c9c7d533', 1317, 1, 'user', '[]', 1, '2021-04-14 05:59:33', '2021-04-14 05:59:33', '2022-04-14 11:29:33'),
('20ac9c36b6d7301ee4ee6dcfa2af44b129d591ce588a8051f57677805af6b003dfb4644e406cf802', 1192, 1, 'user', '[]', 1, '2020-02-07 01:18:29', '2020-02-07 01:18:29', '2021-02-07 06:48:29'),
('20e5e82855540124a2294820d62cce0a04e3742c4af5ff108d23a4fcacc8e5be1d3826ff69906db7', 1329, 1, 'user', '[]', 1, '2021-10-27 05:21:13', '2021-10-27 05:21:13', '2022-10-27 10:51:13'),
('21ccfd4db6453739692f5feba9514d2d88667582f78eb52285829828583ec90a4a412b221edd2630', 1221, 1, 'user', '[]', 1, '2021-04-02 03:33:14', '2021-04-02 03:33:14', '2022-04-02 09:03:14'),
('21cf61ecfaa9df302bd542de14fc383268c6cc88cef82506c036d9ebfe571d0ecfb4f72c828862fd', 1333, 1, 'user', '[]', 0, '2021-10-21 07:11:19', '2021-10-21 07:11:19', '2022-10-21 12:41:19'),
('2215d0d116fd4048d9329e3f575de5040aa9238c2321948d734e4c14974c03e192a81f77f4f2425d', 1264, 1, 'user', '[]', 0, '2021-04-09 02:47:42', '2021-04-09 02:47:42', '2022-04-09 08:17:42'),
('237c3658bc5f6d2bc363b1d0bbee51fd97e959f1e8ea8445bbf710c0d564a65d4745c1991f902b7e', 1192, 1, 'user', '[]', 1, '2020-10-19 23:59:39', '2020-10-19 23:59:39', '2021-10-20 05:29:39'),
('23e59c46a41375bfebf77492d1c643d1ef96dfdb05744824f093d3e71205ff210159348a86a50a33', 1238, 1, 'user', '[]', 0, '2021-04-06 07:18:30', '2021-04-06 07:18:30', '2022-04-06 12:48:30'),
('24c7a022a44c27f1cf4a7e652b1a58f5041c1eac0b76edc92469b3eede38bc6570dd9073e2277f91', 1327, 1, 'user', '[]', 0, '2021-10-07 01:53:39', '2021-10-07 01:53:39', '2022-10-07 07:23:39'),
('2502d3a9a28b1881206cb9498c34726e47b3ca50ea2537de2b0b9785c794ad1f7e66f8cf8354b67b', 1333, 1, 'user', '[]', 0, '2021-10-21 06:27:41', '2021-10-21 06:27:41', '2022-10-21 11:57:41'),
('2613e9cd9a588cf81195021370deed161a46318be2665f6850811c74c6bd54bc451b5967b65ec2ed', 1221, 1, 'user', '[]', 0, '2021-04-02 02:21:06', '2021-04-02 02:21:06', '2022-04-02 07:51:06'),
('265ef8f5c3bc3db5998882b3c6f595ec764a0dc5896fb1bb8fa08481d7ca71f98fa7b0700950296c', 1327, 1, 'user', '[]', 0, '2021-10-07 01:50:07', '2021-10-07 01:50:07', '2022-10-07 07:20:07'),
('27c7647d7818ed9d9ba4e36846d43011840656723d1fb62a0725d58956049a42270cb6d4f02da9a1', 1251, 1, 'user', '[]', 0, '2021-04-07 22:35:21', '2021-04-07 22:35:21', '2022-04-08 04:05:21'),
('28e45d5018a264e88a49028799359077630369b0657c7f494410d26641fc3e99256cd4d925b525c9', 1223, 1, 'user', '[]', 1, '2021-04-05 00:53:20', '2021-04-05 00:53:20', '2022-04-05 06:23:20'),
('291bdaadd287a9b5eab12edea24dcaa8028347af91d7431097cb351656ec66fe2a383c2108150f17', 1298, 1, 'user', '[]', 1, '2021-04-12 23:07:38', '2021-04-12 23:07:38', '2022-04-13 04:37:38'),
('297954616ead6d46e795d5b193ca5e465751a471ab15fc3c68fc154311afc997fd4aaf6836124c06', 1275, 1, 'user', '[]', 0, '2021-04-09 05:24:42', '2021-04-09 05:24:42', '2022-04-09 10:54:42'),
('29c9b406fa6eabbc81af9bf1d55d6981a89191f683400b04c6a7d1c21c8e8cbff3c5330512ab5ef9', 1304, 1, 'user', '[]', 1, '2021-04-13 02:33:37', '2021-04-13 02:33:37', '2022-04-13 08:03:37'),
('2ab9578669b9541d97998fec293a3457324f33ca8e6c84fc37c1024ad412301f4417fcc41bf90a36', 1196, 1, 'user', '[]', 1, '2021-03-31 00:24:38', '2021-03-31 00:24:38', '2022-03-31 05:54:38'),
('2abc3218bee3f1935405d6d7f1da97e0dd31ab04e26394e5c85930b62c9047e085fb6a3679277989', 1289, 1, 'user', '[]', 0, '2021-04-12 13:57:22', '2021-04-12 13:57:22', '2022-04-12 19:27:22'),
('2b68b27e535b33acdbe2792f79f4bf078c277bee12084526c86e5333bf1f3670f3c4a35f54253815', 1221, 1, 'user', '[]', 0, '2021-04-02 02:24:26', '2021-04-02 02:24:26', '2022-04-02 07:54:26'),
('2bcac84d7b6dc565c73c5efdc9a94bc11cc5ecfbdd07cd57dec6ba07c96443270ae505246be5e16c', 1252, 1, 'user', '[]', 0, '2021-04-07 23:14:29', '2021-04-07 23:14:29', '2022-04-08 04:44:29'),
('2bf71b66c816333065ed91d1dfff559a1de7b1eb1fb8d2da91da0012f290c7bad61bee08d52f42a6', 1221, 1, 'user', '[]', 0, '2021-04-02 02:24:47', '2021-04-02 02:24:47', '2022-04-02 07:54:47'),
('2c4f5ec274691d0bfdf82e19e3b3966a4367964b5323750d91feae2bcd33ab189b068a8360321b8a', 1333, 1, 'user', '[]', 1, '2021-10-21 07:14:26', '2021-10-21 07:14:26', '2022-10-21 12:44:26'),
('2cba6063a70f595e02b4865be57ee1fa4f0ea173470e5c1c8e541a76d0428ec4be0162481e04f642', 1291, 1, 'user', '[]', 0, '2021-04-12 22:31:48', '2021-04-12 22:31:48', '2022-04-13 04:01:48'),
('2d0d77480c9ca1c7e88ff3886d6219bd6e994cf19ac49a07fcc3a19052a1ed3ed4f10efeff699d04', 1277, 1, 'user', '[]', 1, '2021-04-13 23:47:30', '2021-04-13 23:47:30', '2022-04-14 05:17:30'),
('2d4e346f9dc5a388f1699ef48f3a4c5d7100b889ee865dc23ba28be246de3b263ae42095c23364b3', 1333, 1, 'user', '[]', 0, '2021-10-21 05:50:24', '2021-10-21 05:50:24', '2022-10-21 11:20:24'),
('2d9e6024e122d1ecf5b55302caa576e6dfc94baf6152164a939b66db9d9a9ed41560b003e12df651', 1215, 1, 'user', '[]', 0, '2021-03-30 23:50:41', '2021-03-30 23:50:41', '2022-03-31 05:20:41'),
('2e32f0c72a913097bd96fc2b51ed5330e9fb2aaf5a43923043081433e9936431d2f1f90a81029ceb', 1275, 1, 'user', '[]', 1, '2021-04-09 05:26:40', '2021-04-09 05:26:40', '2022-04-09 10:56:40'),
('2ed7dc8eebe9eab8de21d12ccc75f35b9e79595c68e824ac535e084e9a1ca99e1c2ed32ca727510f', 1290, 1, 'user', '[]', 1, '2021-10-15 10:19:20', '2021-10-15 10:19:20', '2022-10-15 15:49:20'),
('2edb74cc83fc9ddab058a571adef44f6050ff7c36ce418060b42bf086bbf48219aa43f333f314b99', 1317, 1, 'user', '[]', 1, '2021-04-14 05:34:56', '2021-04-14 05:34:56', '2022-04-14 11:04:56'),
('2fd83c9285a6eab709259dbb032229aa21ce232ba5284ae22c379a1ee7bfa299754bc48173497bc8', 1302, 1, 'user', '[]', 1, '2021-04-12 23:59:12', '2021-04-12 23:59:12', '2022-04-13 05:29:12'),
('3011829aee564ccd6ae7fc1031d7e3f572a5537541947eac1a50f470e0cc57c777e9484ac3890144', 1304, 1, 'user', '[]', 0, '2021-04-13 01:55:56', '2021-04-13 01:55:56', '2022-04-13 07:25:56'),
('30cd6d4ea0bb45695b01988bde1b3a38ecee6f6eac1c9f3d022b8bd89040396a04c6d233a122f34d', 1195, 1, 'user', '[]', 0, '2020-02-07 01:06:00', '2020-02-07 01:06:00', '2021-02-07 06:36:00'),
('3194b1a2c12c7f3f283aa82723b5bcf87da093967eba7ce59c4552ac299155dcd52803b068daec4f', 1261, 1, 'user', '[]', 1, '2021-04-08 23:50:33', '2021-04-08 23:50:33', '2022-04-09 05:20:33'),
('31c986b229278a693ae46a7f11f3b721c85a41b842db46457501e1fcf2126530dffe7b1e414addee', 1290, 1, 'user', '[]', 0, '2021-04-20 06:02:35', '2021-04-20 06:02:35', '2022-04-20 11:32:35'),
('31f1e756e67912fdf26be63e94aee6c38c4399909a3c965821915fd1f38c9f534e54ac328f4b2caf', 1276, 1, 'user', '[]', 1, '2021-04-09 05:44:38', '2021-04-09 05:44:38', '2022-04-09 11:14:38'),
('31f2fe4b8e3eca1d68341d4bf65b265e430766c375ba2e5954aaa8423d76fbcdd9f419f563e6df01', 1198, 1, 'user', '[]', 0, '2020-02-07 05:11:25', '2020-02-07 05:11:25', '2021-02-07 10:41:25'),
('33682caaef04eef710e79fa41b6532cb25ac457e1fb2dd3acd3adf86b9c17192adea65547461bc9d', 1231, 1, 'user', '[]', 0, '2021-04-13 23:19:08', '2021-04-13 23:19:08', '2022-04-14 04:49:08'),
('347a7ede0024f30c11f09818c8543dadce49bf5c4c72a86f41e6cb05453ed8083b84682582bd44b0', 1313, 1, 'user', '[]', 1, '2021-04-14 04:29:17', '2021-04-14 04:29:17', '2022-04-14 09:59:17'),
('348306ec24650e6aae7f3f754a4953e1b9c7afca4ab35a94241f87ea104a3b44e33892d1fef9f2b7', 1268, 1, 'user', '[]', 1, '2021-04-09 03:39:00', '2021-04-09 03:39:00', '2022-04-09 09:09:00'),
('34d4bc7f054af0cc6d3f0e173b0bbc92695c791234eec60a285fd5d681a29b3fa25d9ae73e92977c', 1341, 1, 'user', '[]', 1, '2021-10-28 05:54:48', '2021-10-28 05:54:48', '2022-10-28 11:24:48'),
('37114ab78eeb4389a3f42cbc52a7e99ed279bc98cc63b6cbe1c41a4d844ad5331011cb62d6179f0f', 1267, 1, 'user', '[]', 1, '2021-04-09 03:24:02', '2021-04-09 03:24:02', '2022-04-09 08:54:02'),
('3771cc254f904c9fcba14a77d151b7e40b3940fe43c85aee0eea8c33cc2310317a9b310ddac21dbe', 1223, 1, 'user', '[]', 0, '2021-04-01 01:12:29', '2021-04-01 01:12:29', '2022-04-01 06:42:29'),
('37c61bb9dc867aca1fdb0c8e3248b9f63b32f478a447980edf277b7ff3967d1dff06857b0c5d547f', 1282, 1, 'user', '[]', 1, '2021-04-12 08:08:28', '2021-04-12 08:08:28', '2022-04-12 13:38:28'),
('37f7ed0c5fb2b66fc0bf347f006bce4ace29e6630a3c0f665fc1398837034906e39af9c5dd59337e', 1192, 1, 'user', '[]', 1, '2020-02-07 00:47:57', '2020-02-07 00:47:57', '2021-02-07 06:17:57'),
('39304508ec14babe510ee06de66bbfd2b06a3f4f5f75953182a534f3dcf1b5a1683e89ad226693a2', 1329, 1, 'user', '[]', 0, '2021-10-18 12:46:42', '2021-10-18 12:46:42', '2022-10-18 18:16:42'),
('3ad36571ca35239138a2212e553cbef5768641f98fd89ff16988ecf093233b63faebfe5b13ad2946', 1310, 1, 'user', '[]', 0, '2021-04-14 04:05:14', '2021-04-14 04:05:14', '2022-04-14 09:35:14'),
('3b281b6add7c3582126c7e280ab65f18954ca6a97da0cd8d441b7e43f5fa2f95122c4e625b3f244c', 1205, 1, 'user', '[]', 0, '2020-10-20 05:41:48', '2020-10-20 05:41:48', '2021-10-20 11:11:48'),
('3c1e7bb8b39ba00df39b8411444d3efce793082498051df58a86aa3a5bcec5950e0151e6e4258178', 1193, 1, 'user', '[]', 1, '2020-02-07 01:04:37', '2020-02-07 01:04:37', '2021-02-07 06:34:37'),
('3ccf1357a072aa71f0bce437470daf534e69fec38dad2ed96081fec8c5a98c2c0b57be538d5f212d', 1337, 1, 'user', '[]', 0, '2021-10-26 11:52:43', '2021-10-26 11:52:43', '2022-10-26 17:22:43'),
('3d93c69fff7659aac294ede9016a5119c80ae1c05900d994c11af971b8a031af260111f2d623ca1d', 1276, 1, 'user', '[]', 1, '2021-04-14 01:57:35', '2021-04-14 01:57:35', '2022-04-14 07:27:35'),
('3da304b7977b30f807cdf4bf6cba31692f2c6bb7f2c66d5b5d078bfbcc244e1ac6f7237699ffa1a9', 1266, 1, 'user', '[]', 1, '2021-04-09 03:09:41', '2021-04-09 03:09:41', '2022-04-09 08:39:41'),
('3e30c24aa6b195fe9bcfd9bb006aefa23df2594f79df521c5f4175728cdc22a701e22925768991d4', 1322, 1, 'user', '[]', 0, '2021-04-19 06:36:03', '2021-04-19 06:36:03', '2022-04-19 12:06:03'),
('3ea5395ff5e072e38b9b2c7977cc7ef11f91a6ae280e324e13a4768f95372e9680189a6a857c5aa4', 1318, 1, 'user', '[]', 1, '2021-04-14 05:53:35', '2021-04-14 05:53:35', '2022-04-14 11:23:35'),
('3fcab23066dd420a4205c4fbf71d069ef9fc75007bc6704d3be4a5cad30b37160a0ffdc499588800', 1241, 1, 'user', '[]', 0, '2021-04-06 08:06:58', '2021-04-06 08:06:58', '2022-04-06 13:36:58'),
('40236a2c614fd354f4f97caf43cf6ba471b1b1aac8a3b46dfd13d968fa02a95e8061114593c6b16b', 1240, 1, 'user', '[]', 0, '2021-04-06 08:04:08', '2021-04-06 08:04:08', '2022-04-06 13:34:08'),
('4080d5558d794275f597825ab6529ba71da9f527fb4adf1e49aa4e86cc3a34440687ceb6f66278ae', 1243, 1, 'user', '[]', 0, '2021-04-06 09:25:57', '2021-04-06 09:25:57', '2022-04-06 14:55:57'),
('40d976a9757419900b71d1d7ed4ff729bf0bfc2f7c443926046b8635e8dd8f4b0f93189bb54c3be6', 1205, 1, 'user', '[]', 1, '2021-04-05 01:54:16', '2021-04-05 01:54:16', '2022-04-05 07:24:16'),
('40fb40c4f91ccc3a6462b164c48e7e2356a131f8c5b8dfe630ee0c28147b7087eeb8aca190f1edbc', 1290, 1, 'user', '[]', 0, '2021-04-21 05:16:54', '2021-04-21 05:16:54', '2022-04-21 10:46:54'),
('41102c5791032a9ca59add69c592752c41d11cfffad59f9f8ce5826351bcc8b1179242857943f84e', 1325, 1, 'user', '[]', 0, '2021-10-06 06:31:26', '2021-10-06 06:31:26', '2022-10-06 12:01:26'),
('41929281f1f91f137c1eb1c8201d2d525fe3bf4a3a81b0d685c672b165cc812d7a72de79ed96a837', 1254, 1, 'user', '[]', 0, '2021-04-07 23:49:03', '2021-04-07 23:49:03', '2022-04-08 05:19:03'),
('41d36c2ff35932162a7e9bca2abaee1e8fcde2d5aa6dc719db47c3e346c7f0527dd00e59c2cf4c7f', 1339, 1, 'user', '[]', 1, '2021-10-27 06:56:25', '2021-10-27 06:56:25', '2022-10-27 12:26:25'),
('4237a06f68d2554722635d2e2fc5661749a22316254187c615ef8c4e55fdc2900f393d8c0880ea67', 1209, 1, 'user', '[]', 0, '2020-10-25 12:39:37', '2020-10-25 12:39:37', '2021-10-25 18:09:37'),
('427699b57d29d72c9a699fd2eac4d7e69a66e5e85c7db46b36c929f3e3db6e06efdc4db7066da943', 1231, 1, 'user', '[]', 0, '2021-04-05 23:59:20', '2021-04-05 23:59:20', '2022-04-06 05:29:20'),
('42b49c1a5aff6774989f6035f1279fd17ae21610bddff295809bf9c0cab470cab17ff310c83753d3', 1318, 1, 'user', '[]', 0, '2021-04-16 00:05:45', '2021-04-16 00:05:45', '2022-04-16 05:35:45'),
('4322ce539f20230789399bd3f9f2337ed40999e220af66dc76810d4f41ca89ceef11c02fc822010b', 1213, 1, 'user', '[]', 0, '2021-01-12 00:19:24', '2021-01-12 00:19:24', '2022-01-12 05:49:24'),
('44a6545bcb3842a102956a99b84bece7ea164b18d6ffc07f553cef6f93ce3e76f2e7df92874752c9', 1258, 1, 'user', '[]', 0, '2021-04-08 06:42:48', '2021-04-08 06:42:48', '2022-04-08 12:12:48'),
('44adaded8b0ca0022d46968f8f43add67591b2180c42bbdd16c2620fedf3481c7f0ac0f9af6ecdfd', 1277, 1, 'user', '[]', 0, '2021-04-11 23:23:39', '2021-04-11 23:23:39', '2022-04-12 04:53:39'),
('44faf08dbf972bcf706d1c606938c25f63c1f87ecc2a0c2aea2594dd0fb9f0e8fca38e3da5a619e6', 1332, 1, 'user', '[]', 0, '2021-10-20 06:00:28', '2021-10-20 06:00:28', '2022-10-20 11:30:28'),
('451ab15f89f1ed857be30a8640884c844e3449418e52df05602ef86862b900db5bd85a36ce2abbb0', 1193, 1, 'user', '[]', 1, '2020-02-07 00:51:58', '2020-02-07 00:51:58', '2021-02-07 06:21:58'),
('457509db66028d33ba7e379b34e3a57a0ba7ab781304ec72f9d27fbbc09c2ac14780d2c1c39aec44', 1215, 1, 'user', '[]', 0, '2021-05-05 05:19:14', '2021-05-05 05:19:14', '2022-05-05 10:49:14'),
('45f61575dcf0534caf24ba58347d6f65aec5ee0b2517f312c28eb5098bf5b14b9de313e172278e51', 1288, 1, 'user', '[]', 0, '2021-04-12 13:55:11', '2021-04-12 13:55:11', '2022-04-12 19:25:11'),
('473b3120811d855079155e7056c46dd98a0e0c02bcf76b405cd3f483c73f32f1ea68e3d165fbd6bb', 1285, 1, 'user', '[]', 1, '2021-04-12 08:32:50', '2021-04-12 08:32:50', '2022-04-12 14:02:50'),
('48cb91a6eb58461d55b4283c8f1aefcacec599b18c371b323f65a2d558b8c42ff7b148b7151b0a2b', 1193, 1, 'user', '[]', 0, '2021-04-06 03:56:02', '2021-04-06 03:56:02', '2022-04-06 09:26:02'),
('49a6abcdc1b1d0053d53ab127d113dff78a8aefe2ef6ac39a90b569450a4c994db1138242709991e', 1219, 1, 'user', '[]', 0, '2021-03-31 00:17:30', '2021-03-31 00:17:30', '2022-03-31 05:47:30'),
('4a5a2813e2c7269f999954e7cf4652800130e8bd5352ce7f87975e49f5487616f1d968dcbef0b1c8', 1277, 1, 'user', '[]', 0, '2021-04-11 23:19:28', '2021-04-11 23:19:28', '2022-04-12 04:49:28'),
('4b989a070b04c012717b396efd0e36fd77a0b2185b9e1bf9cacbf2c433c80241cdd9c5b8aa81abf0', 1209, 1, 'user', '[]', 1, '2021-04-01 01:04:46', '2021-04-01 01:04:46', '2022-04-01 06:34:46'),
('4bbda7231147925bf2714cb59a0cd94d82aaa6a7f7f7fcf819a20f7b67f40216768e8d1010db6e58', 1321, 1, 'user', '[]', 1, '2021-04-19 00:17:57', '2021-04-19 00:17:57', '2022-04-19 05:47:57'),
('4c305c53d89a85cd41f7ad48b37c04b8df3452b6694bb8e43aa43c032d24d08265e27051da5dac58', 1344, 1, 'user', '[]', 0, '2021-11-01 06:20:50', '2021-11-01 06:20:50', '2022-11-01 11:50:50'),
('4c39fdaee038abd617e3c64e91662bd2457783b18cf1738d468f5594b88a50d8f2eeb1302a54b88e', 1304, 1, 'user', '[]', 0, '2021-04-13 02:02:00', '2021-04-13 02:02:00', '2022-04-13 07:32:00'),
('4ccce9c5469d56848395cea76593309dc24f4665eee5d1f35227aa927eb65adc0154b44afb4e8600', 1193, 1, 'user', '[]', 0, '2021-04-06 08:37:40', '2021-04-06 08:37:40', '2022-04-06 14:07:40'),
('4cf3f1a6a54f2b53a019740f5c9805311aba04be633561ee395c7bb8672bea7e0d71528bbf26db3c', 1192, 1, 'user', '[]', 0, '2020-10-20 02:00:35', '2020-10-20 02:00:35', '2021-10-20 07:30:35'),
('4d7ebc649cd7a90b045553a7a9d42456137cbc83dde9736f32e46c0d4b5f76fff07bd85ece960c8e', 1293, 1, 'user', '[]', 1, '2021-04-14 01:13:22', '2021-04-14 01:13:22', '2022-04-14 06:43:22'),
('4db6d402a1545c2f50962d118429f1fc74fd7c814a5ca24b54f4beb04fda1b7afe504dfae0db4b4d', 1304, 1, 'user', '[]', 1, '2021-04-13 23:19:34', '2021-04-13 23:19:34', '2022-04-14 04:49:34'),
('4df58b9dc767e1471c9b8047511d6abd7c9010aac74fa5011962b85b7c0c585b3d25e90eae384563', 1231, 1, 'user', '[]', 0, '2021-04-12 03:31:25', '2021-04-12 03:31:25', '2022-04-12 09:01:25'),
('4e178f9e684b3718b7b1e29f6fee6b26232081fb36754d5dc0ae59b26694ec6d79968f55fd77cca0', 1195, 1, 'user', '[]', 1, '2020-02-07 01:09:41', '2020-02-07 01:09:41', '2021-02-07 06:39:41'),
('4ed1bc4389631e94a0d873706ba3a6e7795ad413ca53deccf9937e4b3912c7a245f5d8a96f075a1c', 1290, 1, 'user', '[]', 0, '2021-04-12 15:23:53', '2021-04-12 15:23:53', '2022-04-12 20:53:53'),
('4f86a71c0f7d3fc11858b634e701b42dea90ba6c9ab922cce369518fd839b5fb3417d26e826919a4', 1336, 1, 'user', '[]', 1, '2021-10-26 05:44:31', '2021-10-26 05:44:31', '2022-10-26 11:14:31'),
('5173408fb8813f01bcdf921fb93473c66def86d44ef40d2b7d106d7e16f82f28920222849ef8f13c', 1231, 1, 'user', '[]', 0, '2021-04-13 02:50:58', '2021-04-13 02:50:58', '2022-04-13 08:20:58'),
('517b1da2d212513033708414380c78e99891090cd524103ffa413ffc9964214f6ad11537f975f851', 1299, 1, 'user', '[]', 0, '2021-04-12 23:09:29', '2021-04-12 23:09:29', '2022-04-13 04:39:29'),
('51ae372705772919103eb34f52ae18d97bb52aee71f069cee516348eaaa0fcaffc3ebf6a496ef02c', 1340, 1, 'user', '[]', 1, '2021-10-28 05:46:00', '2021-10-28 05:46:00', '2022-10-28 11:16:00'),
('52495157ef71024e2f3f3245e6e570634fc69e579f481215bbd27d579ac98eb93ec591fba73f2ebe', 1193, 1, 'user', '[]', 1, '2021-04-06 05:56:29', '2021-04-06 05:56:29', '2022-04-06 11:26:29'),
('5265c4fc98232e02341f16da6149e9f194a656ba740f1d9084a2d1c42b4d90f0112db43a87e3f357', 1329, 1, 'user', '[]', 1, '2021-10-26 12:28:01', '2021-10-26 12:28:01', '2022-10-26 17:58:01'),
('54340d4edd7c86365b0154ad0c036b837e7ba09e3fb105c541bcdbda58c197e7d7878325ec6ed3e6', 1329, 1, 'user', '[]', 1, '2021-10-27 05:22:47', '2021-10-27 05:22:47', '2022-10-27 10:52:47'),
('553547c050030cca872beea41f720589f647f4b0b87ff280fc99dad4695a9777934bfee857fda116', 1346, 1, 'user', '[]', 0, '2021-11-02 01:07:43', '2021-11-02 01:07:43', '2022-11-02 06:37:43'),
('55ae3345b7daf38d90f3bfb220e11735c6104d5b3fa2478e990c220ee7c42efcbc2c4bbd734c9303', 1195, 1, 'user', '[]', 1, '2020-02-07 01:20:03', '2020-02-07 01:20:03', '2021-02-07 06:50:03'),
('56c78aeea48c234e735368a661ae0d491ec7646c37bae6676ad3fa0fbac2e7280c302fa12a527a22', 1192, 1, 'user', '[]', 1, '2020-02-07 01:01:15', '2020-02-07 01:01:15', '2021-02-07 06:31:15'),
('5757dd040f6323e86ff92f0d7635b243b8fbb908f838311a7997c038308adac60ab6ef74b5d89aaf', 1193, 1, 'user', '[]', 0, '2021-03-30 23:58:19', '2021-03-30 23:58:19', '2022-03-31 05:28:19'),
('5796089c27adf216ab4a9d0dc04e134a678558e37d527281064960004690973a35ff4902f7bbb51d', 1196, 1, 'user', '[]', 1, '2021-03-31 05:40:13', '2021-03-31 05:40:13', '2022-03-31 11:10:13'),
('5994160415fc6e8df5bfd49f898fefdfead735ab585b9dd1a593c6098a45ca50bc1fe2f2e9070a33', 1339, 1, 'user', '[]', 1, '2021-10-27 06:15:06', '2021-10-27 06:15:06', '2022-10-27 11:45:06'),
('59d2c8510188fcdfe53bf5fcc224bbcb455c4d14b8e45b3bb67df37ba18219dd0345d73e07acb215', 1297, 1, 'user', '[]', 1, '2021-04-12 23:02:34', '2021-04-12 23:02:34', '2022-04-13 04:32:34'),
('5a6c5b430dc2af0a183e271612d5b78829b897c4ea61d5b525381c8d971ab8c1983b70f2b25a383d', 1281, 1, 'user', '[]', 0, '2021-04-12 07:53:28', '2021-04-12 07:53:28', '2022-04-12 13:23:28'),
('5a7eda1575286e34aa27f86905137bdd605ebdd7f1c60b83974dc34c774918adc9338f7646b1732a', 1267, 1, 'user', '[]', 0, '2021-04-09 03:21:33', '2021-04-09 03:21:33', '2022-04-09 08:51:33'),
('5afe9c27134124a795308b4b9205f9aab2939e48d577ee61adfd9096bc693d77694f0c4bc2aaf90d', 1304, 1, 'user', '[]', 0, '2021-04-13 02:16:18', '2021-04-13 02:16:18', '2022-04-13 07:46:18'),
('5c4be0641473136144cca8a1f63ef2685b7f0883eb95f18b22aa4d02f2a92e2da93f47b4210392c8', 1311, 1, 'user', '[]', 0, '2021-04-14 04:06:57', '2021-04-14 04:06:57', '2022-04-14 09:36:57'),
('5d48a9ad5501c0366711d3f8c671843e3900a91e0cb7706a94a6a55799d72e01e9cb66b4e70485e6', 1195, 1, 'user', '[]', 1, '2020-02-07 04:01:19', '2020-02-07 04:01:19', '2021-02-07 09:31:19'),
('5dc682e4c675344d22056336949f695765713b271cfeba8226773f7a16da7427c487d698e39291bf', 1193, 1, 'user', '[]', 1, '2021-04-06 06:47:00', '2021-04-06 06:47:00', '2022-04-06 12:17:00'),
('5ddd95b9a5e868f1f7e73695ccb312fd19f892c1d903704ff9bfaee4e1e0d1ab64abbee2a63f8d35', 1328, 1, 'user', '[]', 0, '2021-10-18 12:16:26', '2021-10-18 12:16:26', '2022-10-18 17:46:26'),
('5e28632021ab955c588f139be650cd635191f2edb376b31504c515352a93ac5df54e482de196c196', 1248, 1, 'user', '[]', 0, '2021-04-07 08:21:35', '2021-04-07 08:21:35', '2022-04-07 13:51:35'),
('5e8532c22fddd094ce7c21ce1348982785d5c03f1b1c0f02b5fbf79287ce960fc3a7971685f917c9', 1276, 1, 'user', '[]', 1, '2021-04-14 01:54:31', '2021-04-14 01:54:31', '2022-04-14 07:24:31'),
('5e8dd5181dbc1bda2274eb4e6718faad7b729c5ab9f3402f3d986a02ecfcf3da63f1435457e9b08e', 1193, 1, 'user', '[]', 0, '2021-04-06 03:51:56', '2021-04-06 03:51:56', '2022-04-06 09:21:56'),
('5ec3c7dbe89d74c36b890b6c1a6cd9287db78aeaea2702621259991c22b3374314ae9f7fe6c3554a', 1214, 1, 'user', '[]', 0, '2021-03-30 07:42:28', '2021-03-30 07:42:28', '2022-03-30 13:12:28'),
('5efe80becb92727126b1636e85b9d32b23a5f2c069fe8be13437a64833917c80e923ef78cdba43d9', 1194, 1, 'user', '[]', 1, '2020-02-07 01:32:32', '2020-02-07 01:32:32', '2021-02-07 07:02:32'),
('609e0406611bd31c27e0470b07c2036101aa60342bc634a0c1800e3da83224fe38dac7e5ada55989', 1333, 1, 'user', '[]', 0, '2021-10-21 05:33:30', '2021-10-21 05:33:30', '2022-10-21 11:03:30'),
('60ef537233378ab5d23c9267f639a698b5f243013445287915c61353b7ab14125322d976a3cf9399', 1195, 1, 'user', '[]', 1, '2020-02-07 01:22:21', '2020-02-07 01:22:21', '2021-02-07 06:52:21'),
('61228249efb00ca613fc518f3d6646dc9147b6f40bf765b6a00c8fce69fbb3339771efe70b1f14e6', 1194, 1, 'user', '[]', 0, '2020-02-07 01:03:47', '2020-02-07 01:03:47', '2021-02-07 06:33:47'),
('612d8c938beab085bbc992fde7b6d69299b3aff01cbace841b86e954c14e6e1df787a128042f4379', 1243, 1, 'user', '[]', 0, '2021-04-06 09:20:55', '2021-04-06 09:20:55', '2022-04-06 14:50:55'),
('617bd632a14a28313d05fe6f018632ea9305d5d1f12a743d97184ce160f6732c0d2b7439ffef4a1c', 1195, 1, 'user', '[]', 1, '2020-02-07 01:07:04', '2020-02-07 01:07:04', '2021-02-07 06:37:04'),
('6347d41863c4dab9ad9917424cd48b79fb294c3ee6fe6af6a8d6b154a537639781455acd287811a4', 1331, 1, 'user', '[]', 0, '2021-10-25 05:09:58', '2021-10-25 05:09:58', '2022-10-25 10:39:58'),
('643f03f739d01f11285d74b3f1c30ddaa3e6a6b944f949d30c7f206ba87f5062e31d63d28f5b5d24', 1327, 1, 'user', '[]', 0, '2021-10-07 01:44:28', '2021-10-07 01:44:28', '2022-10-07 07:14:28'),
('646d66d32f17434c9173eb3e6919ddf15220d85934bd1af4ccad65b3f6016ac913a46e29cd2fba7e', 1332, 1, 'user', '[]', 0, '2021-10-20 06:02:11', '2021-10-20 06:02:11', '2022-10-20 11:32:11'),
('65597e1aa43063f5a57aaaef9f6c7525f05cc1723fc11d9519e2c1e0669f1954f927a4bb4224518c', 1257, 1, 'user', '[]', 0, '2021-04-08 02:17:26', '2021-04-08 02:17:26', '2022-04-08 07:47:26'),
('659109f55d7afd880813944990f12d0df178f16a2f057b6854f75605f0091676fa628d9b9ccb647b', 1223, 1, 'user', '[]', 1, '2021-04-05 02:00:30', '2021-04-05 02:00:30', '2022-04-05 07:30:30'),
('66d1cd0c2591cf74a18f5edd6e4b57abe8d48fdd1c4254294c2ea5816237136fb6fd169ab35224a9', 1265, 1, 'user', '[]', 1, '2021-04-09 02:56:16', '2021-04-09 02:56:16', '2022-04-09 08:26:16'),
('674e63af512cc874362ec3a02bf02e324231011a1804e79faa1095b89f47451649e95c2403895d11', 1221, 1, 'user', '[]', 0, '2021-04-08 00:43:38', '2021-04-08 00:43:38', '2022-04-08 06:13:38'),
('682fc56357f6d9660ef7d48ce84e116a463b57b077758402ce9177e43a983b1236d2ae26e17293e5', 1273, 1, 'user', '[]', 0, '2021-04-09 05:12:40', '2021-04-09 05:12:40', '2022-04-09 10:42:40'),
('6865eb0ce906441086b7cf3059e9aca408438baaf6e3fb225e5a5d8ea7276b332b6c9f601717e715', 1308, 1, 'user', '[]', 1, '2021-04-13 03:49:44', '2021-04-13 03:49:44', '2022-04-13 09:19:44'),
('6873d5729dff9c885fcc279a266e4d6b3b0b2e196f0073398f81fe85b94451793353489651c69165', 1339, 1, 'user', '[]', 1, '2021-10-27 06:40:59', '2021-10-27 06:40:59', '2022-10-27 12:10:59'),
('69ac042ca71f6e4f225f3b18dfda4410ac9bfd593d60f9acbb5a273ce5844c6839ab98db1f82f963', 1339, 1, 'user', '[]', 1, '2021-10-27 06:33:42', '2021-10-27 06:33:42', '2022-10-27 12:03:42'),
('69f3122a96ce1d56d81361d7987d82eda63b51e000847b3f45a943fb091b9a6504db1492ac79f520', 1276, 1, 'user', '[]', 1, '2021-04-09 05:44:02', '2021-04-09 05:44:02', '2022-04-09 11:14:02'),
('6a77bbb7846fd0dbd03de9c2e5466463624514da9240b65eb970bc1ab2332dec3dc7bd3bf7ffdac3', 1338, 1, 'user', '[]', 0, '2021-10-26 12:06:44', '2021-10-26 12:06:44', '2022-10-26 17:36:44'),
('6b06ef86ede7c5bd8ac87d5db0b6c067d84411dd0767aa3b5c55ccfe812db331874fd17aae60d9a4', 1242, 1, 'user', '[]', 1, '2021-04-06 08:27:47', '2021-04-06 08:27:47', '2022-04-06 13:57:47'),
('6c3f665d5aa03e4657d779b57d1cd837cc4b040ce041e3646e2164a7d5578912458f84b751146537', 1228, 1, 'user', '[]', 0, '2021-04-05 00:09:33', '2021-04-05 00:09:33', '2022-04-05 05:39:33'),
('6cc11344b9fe81c7b4b0f51ce55a7858f1e8defebfc12d39e2d9c1ead00142da91087103be0503d5', 1342, 1, 'user', '[]', 1, '2021-10-28 06:27:44', '2021-10-28 06:27:44', '2022-10-28 11:57:44'),
('6d54a28f206bf39979c9195b29324608fc2c4fd719e062ec2bc5bd3394e3428711c6c91af05b88d8', 1333, 1, 'user', '[]', 0, '2021-10-21 06:19:30', '2021-10-21 06:19:30', '2022-10-21 11:49:30'),
('6d8660c894849a78e38a6237125ce8a481a20bed07e8f6f60df6f3996d2f0b5ce72575a0e6e4f921', 1339, 1, 'user', '[]', 1, '2021-10-27 06:12:02', '2021-10-27 06:12:02', '2022-10-27 11:42:02'),
('6e8bee5e71107893e294deb4483ad352dff0e9e85bfc1cba564176e73023086d22167c7ac04bbbf2', 1303, 1, 'user', '[]', 1, '2021-04-13 00:01:03', '2021-04-13 00:01:03', '2022-04-13 05:31:03'),
('6f1821426cb2c16d27526715eb6c207b7d29ff5b318477afa245f976bedeac56e02a5f68903bf152', 1266, 1, 'user', '[]', 0, '2021-04-09 03:08:54', '2021-04-09 03:08:54', '2022-04-09 08:38:54'),
('6f4154a144e68a963599ba59d84fba650e7161207eef8d7919b53ee873001d6af10981d07ab2e102', 1193, 1, 'user', '[]', 0, '2021-04-06 04:04:29', '2021-04-06 04:04:29', '2022-04-06 09:34:29'),
('6f58097cc33c5da8d185b3d1d28ea4953561f78e37ee6f2f698b87c35dc76cdda198e4ad3b3995d4', 1231, 1, 'user', '[]', 0, '2021-04-13 02:59:10', '2021-04-13 02:59:10', '2022-04-13 08:29:10'),
('6ff9329473ebf6e6ce3782400d543aa7363ec662cd24f25a53617a6dc8884ead89e6c2e34296b334', 1324, 1, 'user', '[]', 0, '2021-10-28 14:00:33', '2021-10-28 14:00:33', '2022-10-28 19:30:33'),
('70195b1e61c5aef0cd574f0f0f416eff943bdde9fde1c9b7165b6ea1baf152b5992ed67b4b49e585', 1196, 1, 'user', '[]', 1, '2020-02-07 04:10:01', '2020-02-07 04:10:01', '2021-02-07 09:40:01'),
('701a54fe8e819a629fd4d9b97327031393d6e58ddd3cf8f25235fa3ac481bb7c39757eac3856498a', 1326, 1, 'user', '[]', 0, '2021-10-14 01:35:01', '2021-10-14 01:35:01', '2022-10-14 07:05:01'),
('705884f5bb62e9fd458b1d175f0875949b9351abe8768425db2b33c3f23236dbe6660d7fe5148749', 1338, 1, 'user', '[]', 0, '2021-10-28 14:04:11', '2021-10-28 14:04:11', '2022-10-28 19:34:11'),
('70c7fb96dd00d611fe1b39ab33403c8bfaafae759a6eef2aaf00557f1122c17af0d0813e3a2c6621', 1333, 1, 'user', '[]', 0, '2021-10-21 05:59:38', '2021-10-21 05:59:38', '2022-10-21 11:29:38'),
('70ceba7de45527148e27a715501ed2e5d1e0ad8b8ef926f24bc66f0e4ca13433facf9c80a770770e', 1302, 1, 'user', '[]', 0, '2021-04-12 23:58:27', '2021-04-12 23:58:27', '2022-04-13 05:28:27'),
('7189476a64441d67afdfa18012313786af51bd5e2a0f7108156738b6bb5de38d2ab6e622592b8eba', 1205, 1, 'user', '[]', 1, '2020-10-20 05:42:51', '2020-10-20 05:42:51', '2021-10-20 11:12:51'),
('726489ad944633070645c673547c565501b45f790b54c1a4e6657edb0ba9329dfb6e0ba8f174087d', 1332, 1, 'user', '[]', 0, '2021-10-25 05:12:27', '2021-10-25 05:12:27', '2022-10-25 10:42:27'),
('72fccabfcaaaf96520f714b97864369cde34cf607117db3a87543ba19442eeab7e0dda43968b2b3d', 1212, 1, 'user', '[]', 0, '2021-01-11 23:40:21', '2021-01-11 23:40:21', '2022-01-12 05:10:21'),
('7378cc294fff7b2ed00185db8d627f8a2078a66de72eccbf7acafca799ff6aeb7f5c8e1943a41be1', 1279, 1, 'user', '[]', 1, '2021-04-14 05:22:44', '2021-04-14 05:22:44', '2022-04-14 10:52:44'),
('738674b74b4d2fcc4174f6a2a2d5b351a92914e3ac092ed60bb3f39f1d585614db6fffad28be0284', 1339, 1, 'user', '[]', 1, '2021-10-27 07:00:45', '2021-10-27 07:00:45', '2022-10-27 12:30:45'),
('7391bce3f7dadb2c146a1cfe7a28c1bbbe058f92f8dd067914d87f5d188a0c7c70b5accbfec1784b', 1223, 1, 'user', '[]', 1, '2021-04-11 23:47:00', '2021-04-11 23:47:00', '2022-04-12 05:17:00'),
('739ca706b7999c955f32325401fe825350028548b31fb83d17f403008241d9913ba3c3f47e966390', 1193, 1, 'user', '[]', 0, '2021-04-06 05:47:26', '2021-04-06 05:47:26', '2022-04-06 11:17:26'),
('73cfce03ffea5f1371c892f19bb91fa841d2bf467039dd31b4191b4bfe279baec82ec968ca6044aa', 1221, 1, 'user', '[]', 0, '2021-04-02 01:05:10', '2021-04-02 01:05:10', '2022-04-02 06:35:10'),
('7479bef3728c520ee62b781ba5ad49627f097a2f8bd1e72cff23976eb09f465ac3c0f310acd2ff29', 1303, 1, 'user', '[]', 1, '2021-04-13 06:54:57', '2021-04-13 06:54:57', '2022-04-13 12:24:57'),
('749ddb82f8f60701edd51e5d6d4b75aa437c804640453169013f419cb5cb1ff4d43694d86a4bd5c8', 1320, 1, 'user', '[]', 1, '2021-04-14 06:45:23', '2021-04-14 06:45:23', '2022-04-14 12:15:23'),
('74b86e4fb23d95da1db0fdbd0fc5ff632eb64312b99b5299edb2c86af15cbb5c6d850efc623c1d6d', 1203, 1, 'user', '[]', 0, '2020-10-20 02:04:43', '2020-10-20 02:04:43', '2021-10-20 07:34:43'),
('74cbc278f557fbd0a5b14ace5d58a3ea952f91e35a4f9d49002ce16f7c319b94017d1258c3dd2102', 1222, 1, 'user', '[]', 1, '2021-03-31 05:35:27', '2021-03-31 05:35:27', '2022-03-31 11:05:27'),
('7568d427f0f42951fd074ca1b0e183a98fc8cd71cbc3409bc0372d162850397c502de6a5d0234d55', 1306, 1, 'user', '[]', 0, '2021-04-13 02:36:45', '2021-04-13 02:36:45', '2022-04-13 08:06:45'),
('762abb3711225ec489a5b2c2bc679670b50477d6cc5ee654aa6b42e1f7a83d776e0c3672bf7641d1', 1339, 1, 'user', '[]', 1, '2021-10-27 06:45:37', '2021-10-27 06:45:37', '2022-10-27 12:15:37'),
('7772fe5eaf5141d1db2ac0495d9ee2d3a859b2230e0c75cbd6caec16c6584961267cfbcb04e29fbf', 1214, 1, 'user', '[]', 0, '2021-03-30 07:42:32', '2021-03-30 07:42:32', '2022-03-30 13:12:32'),
('7782d60ad5795889ef94f449ba3c29d9cf1336b55226bee0cf336410d74cbec320c99a6e3c5858b9', 1333, 1, 'user', '[]', 0, '2021-10-21 06:43:13', '2021-10-21 06:43:13', '2022-10-21 12:13:13'),
('7789db8b8beb450c1912ef8cbb5bb3f4d046e23d3ef7d98808cf648003bcef6d766e369260730c45', 1329, 1, 'user', '[]', 1, '2021-10-26 05:53:06', '2021-10-26 05:53:06', '2022-10-26 11:23:06'),
('7874489f3c8556f5c7eb46ca04af97b6fb2bbb5ce013411222c3eda9a504c61e8fcf78e51ade2e59', 1337, 1, 'user', '[]', 1, '2021-10-26 12:23:56', '2021-10-26 12:23:56', '2022-10-26 17:53:56'),
('788bd2c6a350890e19d63d3e6be56a246bbc7685cb4754d8adb3b2a16ec8d7b17e6b7da5d9f308fc', 1299, 1, 'user', '[]', 1, '2021-04-13 23:46:58', '2021-04-13 23:46:58', '2022-04-14 05:16:58'),
('78987644ef63bebbfa5b1451cecd92843308fb2ce549266abcc26506ab12b9ae3de78f86d3d3849f', 1246, 1, 'user', '[]', 0, '2021-04-07 07:48:23', '2021-04-07 07:48:23', '2022-04-07 13:18:23'),
('79aeb2aec3471185437bacbf861212c0ae4eb59dd6964e958c2d4d061989a73e3ccbbed1748e8105', 1332, 1, 'user', '[]', 0, '2021-10-20 06:03:47', '2021-10-20 06:03:47', '2022-10-20 11:33:47'),
('79b740c8a10780432d19414246061d3f3e2c32b93169d4821ae45a837f0e44677c778f8fee674990', 1203, 1, 'user', '[]', 0, '2020-10-19 23:25:03', '2020-10-19 23:25:03', '2021-10-20 04:55:03'),
('7ba932b2d42b81b9e41d5bcca75681bc0b86fb33806da3785c2fd507cca606b01cfdf333732ec4e9', 1331, 1, 'user', '[]', 1, '2021-10-27 05:30:16', '2021-10-27 05:30:16', '2022-10-27 11:00:16'),
('7bf097e5fabbb72c0fbb3e87d826040426d969e3fc53c6155d4efa7f3f200cfd8b37ae997844ed34', 1193, 1, 'user', '[]', 0, '2021-04-19 00:09:17', '2021-04-19 00:09:17', '2022-04-19 05:39:17'),
('7c0122004acc47452433e78de3d50c9d15507c156ee629e10e0d768ec57043a9276cddeca0b3cde1', 1232, 1, 'user', '[]', 0, '2021-04-09 05:56:42', '2021-04-09 05:56:42', '2022-04-09 11:26:42'),
('7ca2dfa9d6f354c9eb4cc94dc6c19dd724afe10829e6ab1106a8cc0171f81957b9b68f7b2e32af8d', 1269, 1, 'user', '[]', 0, '2021-04-09 03:50:06', '2021-04-09 03:50:06', '2022-04-09 09:20:06'),
('7d2caa18887a55656be7c84e65803ecb6f057c2ea22a4f308eefe56389d6f31a767425d50bb8a21b', 1306, 1, 'user', '[]', 1, '2021-04-13 02:38:42', '2021-04-13 02:38:42', '2022-04-13 08:08:42'),
('7d9b8618208789f58fb3afec1f891bad46657bb9f6f3216d9572703bbec39272cfd5a0c60faa1fca', 1261, 1, 'user', '[]', 0, '2021-04-08 23:48:56', '2021-04-08 23:48:56', '2022-04-09 05:18:56'),
('7eccb91a38bf9442c1955ad9d3fa2a29c314d418ef3f59894f72ae642ecd042b94cddcaaed5f3c7c', 1314, 1, 'user', '[]', 0, '2021-04-14 04:38:04', '2021-04-14 04:38:04', '2022-04-14 10:08:04'),
('7ef38f0ab46b0dbd8d6ed46378ed44b51155cbaa7ca57a49fdf79bb2cbf138d6f8390c81b1c7b506', 1319, 1, 'user', '[]', 0, '2021-04-14 06:02:07', '2021-04-14 06:02:07', '2022-04-14 11:32:07'),
('7fc7f0c43484e9ea6a0b94d38ce7444c00b3b50b32ae0acea24d1adcdaf67e8a8e7722cf3325fa58', 1347, 1, 'user', '[]', 0, '2021-11-06 11:50:10', '2021-11-06 11:50:10', '2022-11-06 17:20:10'),
('8056878f8104c51b9b4988a437a9c40a0339ea7a03f695cbb9955e947c99ff850284f50732ee8a8d', 1197, 1, 'user', '[]', 1, '2020-02-07 04:57:54', '2020-02-07 04:57:54', '2021-02-07 10:27:54'),
('809cbe81656f2ff8779ceaad7e314223fc1665499b96214dc489bfc0c681d501a07626d3fa8fb5fd', 1279, 1, 'user', '[]', 0, '2021-04-12 07:37:49', '2021-04-12 07:37:49', '2022-04-12 13:07:49'),
('80c8e8db7b6d1e3b8cb0599fb2a11c7c8c5a2980060ff60a2b67ce335756fefc2c25897f21568863', 1333, 1, 'user', '[]', 0, '2021-10-21 06:31:45', '2021-10-21 06:31:45', '2022-10-21 12:01:45'),
('8107a589d7dae056abd4e9fdf90628934e2e5ec3bd049e14eb9e430f5f75e40e5d28d54bf1db7438', 1289, 1, 'user', '[]', 0, '2021-04-12 13:56:12', '2021-04-12 13:56:12', '2022-04-12 19:26:12'),
('81a316e353d371d2b3b36e7e90ed52c26ea3593737b966ac1d6e458141a84f107dd95815428721b9', 1312, 1, 'user', '[]', 1, '2021-04-14 04:25:04', '2021-04-14 04:25:04', '2022-04-14 09:55:04'),
('81b20250d224540da6a0fa5d620bca01160c1fff355e1cd6ae01d1935688e4739860e2afaba52ef1', 1270, 1, 'user', '[]', 1, '2021-04-09 04:05:21', '2021-04-09 04:05:21', '2022-04-09 09:35:21'),
('81de5ff44ce4b51110bd9b73615ad9070a77f7c19994980e035dc414f3b9f26d8c17d841a19dd6f5', 1249, 1, 'user', '[]', 0, '2021-04-07 08:24:34', '2021-04-07 08:24:34', '2022-04-07 13:54:34'),
('82cde767fd8bdf95c26aae18aaf178a21b4adc8029a1d104151d59d717588a0af41d694e052ac233', 1340, 1, 'user', '[]', 1, '2021-10-28 05:36:28', '2021-10-28 05:36:28', '2022-10-28 11:06:28'),
('846169e1a0f90e593fbcceb559ad34262e9a229b30fe0e8d2c637307f1e2d9e4a122ca6dee5a044e', 1289, 1, 'user', '[]', 0, '2021-04-12 13:55:42', '2021-04-12 13:55:42', '2022-04-12 19:25:42'),
('849cb0702743d0b51cf5ce30c4f6957be61eb2d0511dbb631aa60d66a04af4f4f0dfb15d4b9a0886', 1205, 1, 'user', '[]', 0, '2020-10-20 05:41:37', '2020-10-20 05:41:37', '2021-10-20 11:11:37'),
('84c1feff98a8687403d0c8c9f30c14357ed8603a285bed083c1a4b6ed8be92d5f3662c035b08dec6', 1198, 1, 'user', '[]', 0, '2020-02-07 04:56:12', '2020-02-07 04:56:12', '2021-02-07 10:26:12'),
('84f76606d9a6c148f93f2ff0e2d52da631ea1bdf3ba5de5df9cc223b1b9f793aa1218b6aaf01c543', 1262, 1, 'user', '[]', 0, '2021-04-09 02:18:10', '2021-04-09 02:18:10', '2022-04-09 07:48:10'),
('85529ba89ec8b3ecee0ac2d6a2d9e7796df551f182846fe6c0fc902d76736abb393605efede49fa7', 1192, 1, 'user', '[]', 1, '2020-02-07 00:51:04', '2020-02-07 00:51:04', '2021-02-07 06:21:04'),
('856d046177dde13d42eecf2474d75dbdbd698d103a1981bd5f503561bcf7ca1cd571d001a0284a5b', 1325, 1, 'user', '[]', 0, '2021-10-06 06:28:26', '2021-10-06 06:28:26', '2022-10-06 11:58:26'),
('859ece7969048ebf774f0bd6e29e865e694f010f7c5d059c94ec2b895ec5121a56154f38399034c6', 1193, 1, 'user', '[]', 1, '2020-02-07 00:53:04', '2020-02-07 00:53:04', '2021-02-07 06:23:04'),
('85d9397b3a355fccf2f4ee230638c809fd0c3c5f0f307cc8e1e9c8ba154317d7f0415c0b526ce423', 1232, 1, 'user', '[]', 0, '2021-04-05 23:59:59', '2021-04-05 23:59:59', '2022-04-06 05:29:59'),
('86fb290ecaf71273db869b7808df918fea86d9dc537e03fb5533810fd1137679c72e4e99fa4317bb', 1299, 1, 'user', '[]', 1, '2021-04-14 01:46:24', '2021-04-14 01:46:24', '2022-04-14 07:16:24'),
('87042cc501dab6d9cf2827f717e20411fc5e24d8920be0d3fb57a0ca3519e93b8588f0b653701fc4', 1299, 1, 'user', '[]', 1, '2021-04-12 23:42:15', '2021-04-12 23:42:15', '2022-04-13 05:12:15'),
('87561292e95ad5c097afab51802d935da9e0e83c31491c563083f72b6f4c3b02f640803fad49577b', 1221, 1, 'user', '[]', 1, '2021-04-05 00:54:13', '2021-04-05 00:54:13', '2022-04-05 06:24:13'),
('87b13776578e3a6a350c6430502f6809b57eb0f2f791689a1a82b43e793987b43bfdac2ba459baeb', 1334, 1, 'user', '[]', 0, '2021-10-21 05:15:03', '2021-10-21 05:15:03', '2022-10-21 10:45:03'),
('87ce5e51dcf443f6d2a2a9d45b0ac6923eadc775a1bd6056101d5cb388a53ec0cda0cef1fcd158ed', 1314, 1, 'user', '[]', 1, '2021-04-14 04:38:49', '2021-04-14 04:38:49', '2022-04-14 10:08:49'),
('87ebc991e30503e982abdb10d842a942899c94a986daeb3c92b0ebc417afbacb11a2bb9fa96642b5', 1223, 1, 'user', '[]', 0, '2021-04-01 22:45:42', '2021-04-01 22:45:42', '2022-04-02 04:15:42'),
('88578f40b59c82ca0c146357cbb3aa07420bdf2091a093a0d872677162468ad0dc474b5b629373d1', 1293, 1, 'user', '[]', 1, '2021-04-12 22:37:15', '2021-04-12 22:37:15', '2022-04-13 04:07:15'),
('88f7ad23fbd211cf596c1fb03d51bd23254546417efdc5286593d3d4c419960cf0699edda6c82c27', 1318, 1, 'user', '[]', 0, '2021-04-14 05:37:19', '2021-04-14 05:37:19', '2022-04-14 11:07:19'),
('894076898e7d6ac683a3e0337d392a1114f6541e9ff77c6cbd95f30ca541a714603c059a547f6b69', 1337, 1, 'user', '[]', 1, '2021-10-28 13:56:37', '2021-10-28 13:56:37', '2022-10-28 19:26:37'),
('895e2f84dd5d9a6a668906ef417fc0d6c94f33ef282cc35e4349203b95e6ceda629e7b1ed1a9ed27', 1261, 1, 'user', '[]', 1, '2021-04-21 07:17:07', '2021-04-21 07:17:07', '2022-04-21 12:47:07'),
('89a1e6914fbf0083e4e3ddfb45b8c67ab09701e25880b8a757da5cc09e1050c414afdff1fbdd9f10', 1347, 1, 'user', '[]', 0, '2021-11-06 11:48:28', '2021-11-06 11:48:28', '2022-11-06 17:18:28'),
('8a0629064ebfc4a713ab7f1322ffef8b4b2c02c38171ac9c7c969c4d386535338e6f9c0382712f1c', 1339, 1, 'user', '[]', 1, '2021-10-27 06:28:50', '2021-10-27 06:28:50', '2022-10-27 11:58:50'),
('8a17a318518f79415f0b6c6ab64a24365888dd0802a0badc1c9f4001c3e5de9eb07c21ddf5386fec', 1339, 1, 'user', '[]', 1, '2021-10-27 06:25:00', '2021-10-27 06:25:00', '2022-10-27 11:55:00'),
('8a442194ae7027bb73b5ec513bd435233ef6bb00f14723056af2d9c468b27487633487d514cf872f', 1333, 1, 'user', '[]', 0, '2021-10-21 05:28:07', '2021-10-21 05:28:07', '2022-10-21 10:58:07'),
('8a972c2e81a6c34c121f8cd4a6f8d29b3f78b56d9054233a7dde3d52e87fe36e69904ad4032218a7', 1307, 1, 'user', '[]', 0, '2021-04-13 03:06:57', '2021-04-13 03:06:57', '2022-04-13 08:36:57'),
('8b932c6d1b0b50884528df9f61c797b498a188e53788c76455e13e5a7995cdb16fe07a4902f3d7ca', 1193, 1, 'user', '[]', 0, '2021-04-06 04:07:34', '2021-04-06 04:07:34', '2022-04-06 09:37:34'),
('8b9a1573aeff41ab13f428bfba83e318ef8ef69eb011462a5cf862246d9e14659f18ddd6b8156391', 1242, 1, 'user', '[]', 0, '2021-04-06 08:25:55', '2021-04-06 08:25:55', '2022-04-06 13:55:55'),
('8c0a2a686683a949e95a260297f6f1d46a89dd6cca0e4f1df54267c8eb2470766e003eb44506c52f', 1298, 1, 'user', '[]', 0, '2021-04-12 23:04:51', '2021-04-12 23:04:51', '2022-04-13 04:34:51'),
('8cab65d95978669791fdbf4ab42d0a50c0f83c9812ce6c43d22aa27faf070fcec909512bff301571', 1193, 1, 'user', '[]', 0, '2021-04-06 05:43:47', '2021-04-06 05:43:47', '2022-04-06 11:13:47'),
('8e17081215a0c26943ba883a346ac3b8f7823b19aec89ba5282218588791869fcf48355c09629eba', 1278, 1, 'user', '[]', 0, '2021-04-13 01:48:57', '2021-04-13 01:48:57', '2022-04-13 07:18:57'),
('8ebc637c04be8f847938173c8254922a7554298fab7a7f9e622dd9210836f2734341469f922eb331', 1221, 1, 'user', '[]', 0, '2021-04-09 05:57:10', '2021-04-09 05:57:10', '2022-04-09 11:27:10'),
('8f1769cac6f93dae1794a4e205d68b3132a18a7c9a2b90c4a57492eabad22438a195d074b105bfb7', 1198, 1, 'user', '[]', 1, '2020-02-07 04:57:18', '2020-02-07 04:57:18', '2021-02-07 10:27:18'),
('8ffb70a817f3a05d221a17aa40c6a6b931d2df6459c5cd757ac66a63c5bdd1631114914b221d422f', 1300, 1, 'user', '[]', 0, '2021-04-12 23:46:02', '2021-04-12 23:46:02', '2022-04-13 05:16:02'),
('909b8c05d09b3df77c5953108e36b9ef15c329bcc944de671c9131208fdc61873d9380b0cb22ac97', 1304, 1, 'user', '[]', 0, '2021-04-13 02:10:01', '2021-04-13 02:10:01', '2022-04-13 07:40:01'),
('928a409da3231258ed7a595ab882fc860b45a65299e818e914b75a8e5efcc64d11c426ee62bab1fd', 1340, 1, 'user', '[]', 0, '2021-10-28 05:34:34', '2021-10-28 05:34:34', '2022-10-28 11:04:34'),
('9323afce36bd24672f2e49d83d29e5fedf5ead261253b9be4ee01e384cae3b72477d0cf4a2d2410e', 1329, 1, 'user', '[]', 1, '2021-10-26 12:12:26', '2021-10-26 12:12:26', '2022-10-26 17:42:26'),
('93d8661ea9a4f8a724061940ca7564e02147a6401f65392ec6b2916723cd4d3fec809254bfc591fc', 1193, 1, 'user', '[]', 0, '2021-04-06 04:01:53', '2021-04-06 04:01:53', '2022-04-06 09:31:53'),
('94787e24079667c46f4966e2c384412eeca27901080f5fd101daf8ee39dd95b1f16c38ffc92a78b8', 1304, 1, 'user', '[]', 0, '2021-04-13 01:58:52', '2021-04-13 01:58:52', '2022-04-13 07:28:52'),
('94796ab5bb32e094c265950d7bbc6f1dfe793ba9e73fe9463cb2bd2bcb0368298184e9e44b45999a', 1194, 1, 'user', '[]', 0, '2020-02-07 01:07:28', '2020-02-07 01:07:28', '2021-02-07 06:37:28');
INSERT INTO `oauth_access_tokens` (`id`, `user_id`, `client_id`, `name`, `scopes`, `revoked`, `created_at`, `updated_at`, `expires_at`) VALUES
('94ba2d9dfd0b13860594936691a3839da47ed9319326eb99adac8a71e85f78d75b9ef59819a1ebc8', 1214, 1, 'user', '[]', 0, '2021-03-30 07:31:56', '2021-03-30 07:31:56', '2022-03-30 13:01:56'),
('951a76d5d0ebf76cab1104770da8ef3ad2bdf5c1f89cbe0b7ff7d0c523b67684d5e7f559fac3ad90', 1333, 1, 'user', '[]', 0, '2021-10-21 05:47:16', '2021-10-21 05:47:16', '2022-10-21 11:17:16'),
('956463ba246aef25ba49c17bf98072b20cc6845ce472aa32ed480ecd9fd7673f5ee85e39fbb55a1f', 1333, 1, 'user', '[]', 1, '2021-10-21 07:12:12', '2021-10-21 07:12:12', '2022-10-21 12:42:12'),
('9630eacbae5c5477b53ca64526d1ecdbc113c8b3dbf57b434a896c63e7052958051b176eaf7ed033', 1290, 1, 'user', '[]', 0, '2021-04-21 07:32:24', '2021-04-21 07:32:24', '2022-04-21 13:02:24'),
('97661f59d68f22e4194e898ca646ceffdaa05d282d7b3bb8210a3aad9ee5ed817e68b33d455706bc', 1333, 1, 'user', '[]', 0, '2021-10-21 05:15:31', '2021-10-21 05:15:31', '2022-10-21 10:45:31'),
('985c271590e02be628360f71179ad499bae9d070b4308641711e57fe6452e8d54b2dcf24ac6e36b8', 1192, 1, 'user', '[]', 1, '2020-02-07 01:31:07', '2020-02-07 01:31:07', '2021-02-07 07:01:07'),
('9866ee97d594a9c719712362875e30660f981883976b9f8fd57b44279dc8f057fefa51891f663fc8', 1228, 1, 'user', '[]', 0, '2021-04-07 07:13:15', '2021-04-07 07:13:15', '2022-04-07 12:43:15'),
('98982c9175a3162c5725b5a1359287475b762009f2f933df265d244c505ba8f6ae6ca91753d6ce22', 1204, 1, 'user', '[]', 0, '2020-10-19 23:49:58', '2020-10-19 23:49:58', '2021-10-20 05:19:58'),
('98f7f45c9d254f79d1c9bddf9e33f475bef789c746c99831b3837422485890e57f9be7dca9acc450', 1292, 1, 'user', '[]', 0, '2021-04-12 22:32:35', '2021-04-12 22:32:35', '2022-04-13 04:02:35'),
('99116991a69e705fe1ece95f4a32f42e5836e099a1ff6903ccdaf9574e581c25e156c75113d08332', 1215, 1, 'user', '[]', 0, '2021-05-05 05:16:05', '2021-05-05 05:16:05', '2022-05-05 10:46:05'),
('9a4c633f240317c199afd388f3db2991e4f66933857b5657860e091140fbba579b02786c54a85b00', 1223, 1, 'user', '[]', 0, '2021-04-02 01:02:49', '2021-04-02 01:02:49', '2022-04-02 06:32:49'),
('9ac09ac4d2db1d51a747f4d371d06a408b08c59d4b06cc8e8c4c7fb94d212b0b1de578b528074362', 1309, 1, 'user', '[]', 0, '2021-04-13 10:20:50', '2021-04-13 10:20:50', '2022-04-13 15:50:50'),
('9af341a9bfc4fbbe1dde66cb5c1de04ece1e5fb17e26103c411be24e4b4c56fce487ae9347fd9f15', 1327, 1, 'user', '[]', 0, '2021-10-07 01:46:40', '2021-10-07 01:46:40', '2022-10-07 07:16:40'),
('9bedf90beac14fae2b69d2ef0e22995331e91a034d7a5d505b71caf9819095126a77b7c074fae294', 1315, 1, 'user', '[]', 0, '2021-04-14 04:50:10', '2021-04-14 04:50:10', '2022-04-14 10:20:10'),
('9bfa1e3cbb007f751169308a4d6a0a3ba087aaec1d88774775878582dc0a94e8f761312a08279d18', 1345, 1, 'user', '[]', 0, '2021-11-01 06:13:40', '2021-11-01 06:13:40', '2022-11-01 11:43:40'),
('9c276e8ea0e3c14c6e749bc14a7947c2eef11a4f71f2318e2a2baab2ada474ad1fbefdc19d5502ca', 1329, 1, 'user', '[]', 1, '2021-10-28 13:53:21', '2021-10-28 13:53:21', '2022-10-28 19:23:21'),
('9c36a5d997eb5c298185d889a771af7a499a4ab4fb42288ab92acc95d1e5a1a29ee7d831799f4859', 1297, 1, 'user', '[]', 0, '2021-04-12 22:54:58', '2021-04-12 22:54:58', '2022-04-13 04:24:58'),
('9c84fd67ab262812b36da5afa0848d6b8f7279aca277f480a4d1c4aad9bdbdb0db4e03a78f8b5f1b', 1222, 1, 'user', '[]', 0, '2021-03-31 03:13:49', '2021-03-31 03:13:49', '2022-03-31 08:43:49'),
('9c8eea57095439bc38afe1470e3f13bafcb4f689a7ab700655089cd841ab8aec941621eab09024f6', 1315, 1, 'user', '[]', 1, '2021-04-14 05:00:01', '2021-04-14 05:00:01', '2022-04-14 10:30:01'),
('9d0acae20447860deace8dbfc701c10fdf0947e5c4d121332bff80fed6f1617db2a8f740aa35d1d9', 1194, 1, 'user', '[]', 1, '2020-02-07 04:02:31', '2020-02-07 04:02:31', '2021-02-07 09:32:31'),
('9d9f22c5c012dcb1839d9270f43486974ca878a4df5396fdf4423164ef4601d22e2a3a6308103e4a', 1209, 1, 'user', '[]', 0, '2020-10-25 12:41:31', '2020-10-25 12:41:31', '2021-10-25 18:11:31'),
('9e69ba6e5d525d59f5c036b6caaf6c7940fdce6788d00c8af17893e37567df4abed030d96bda5a70', 1239, 1, 'user', '[]', 0, '2021-04-06 08:01:06', '2021-04-06 08:01:06', '2022-04-06 13:31:06'),
('9e6b2624895b01b90e00ae425aba4df44ea86abda44886f1bf788567fc8a07b40df8ea31ef657686', 1333, 1, 'user', '[]', 0, '2021-10-21 06:12:35', '2021-10-21 06:12:35', '2022-10-21 11:42:35'),
('a00cb354f922376b36509d0986105196dcfb0f38bf4c140246488c850bfe2fef1a3f3ab27925d10a', 1345, 1, 'user', '[]', 1, '2021-11-01 06:12:02', '2021-11-01 06:12:02', '2022-11-01 11:42:02'),
('a133305b50ac426abfe19816f1475fcf8b2fae838e90bec2f635d8e0938f3f5ffebdbce5f85389c9', 1343, 1, 'user', '[]', 1, '2021-10-28 06:26:39', '2021-10-28 06:26:39', '2022-10-28 11:56:39'),
('a147e0c1133468f6cdb21d8ccf6155059bc30ce0a0f5a2b9c66183ca3bc58035cb02e8fa02e06dfa', 1333, 1, 'user', '[]', 0, '2021-10-21 04:46:45', '2021-10-21 04:46:45', '2022-10-21 10:16:45'),
('a16f122b9bab1c9cfc195d1b9c7fe99e2e895ac7c7b8e26b7bc9824409ba97945efdf132734be341', 1264, 1, 'user', '[]', 1, '2021-04-09 02:48:48', '2021-04-09 02:48:48', '2022-04-09 08:18:48'),
('a1acc495c0783c9712f52d2bacf6bee4d11cc3382a63d4ef4e5bfcc872de4ad637ad4d6dcfc21eb7', 1338, 1, 'user', '[]', 0, '2021-10-26 12:09:06', '2021-10-26 12:09:06', '2022-10-26 17:39:06'),
('a249e470b73ad97f96c07f6311047de26d2e4d018b86a0a663760afbee498dbed4ae60948c27ca41', 1345, 1, 'user', '[]', 1, '2021-11-01 06:07:33', '2021-11-01 06:07:33', '2022-11-01 11:37:33'),
('a3482f22b8823bd6e9179dc755b7a61428b12d29a3ef25da41c8260ab9409889aeefb94bcb5ab541', 1343, 1, 'user', '[]', 1, '2021-10-28 07:25:09', '2021-10-28 07:25:09', '2022-10-28 12:55:09'),
('a3d0ffae7d3916b7e777cb4dd9348b04f4f17781559c9633ee5d5275635b6d0adb66663d3986b652', 1317, 1, 'user', '[]', 0, '2021-04-14 05:34:06', '2021-04-14 05:34:06', '2022-04-14 11:04:06'),
('a3f2c6242c8e05b2485ab519e71573c85afbb5f622f27b05381e92d93146748ca237ba3208ab3a26', 1339, 1, 'user', '[]', 1, '2021-10-27 06:53:49', '2021-10-27 06:53:49', '2022-10-27 12:23:49'),
('a4d75d00855d32b29e94a744a3c7ba2fcf4f59d084532be6a469eda0e3ebc8b2579b7e6f26b3866c', 1323, 1, 'user', '[]', 0, '2021-04-20 05:13:00', '2021-04-20 05:13:00', '2022-04-20 10:43:00'),
('a533e8a644e4dda7bea31ea977ecee58ea4c2186c8e0c017d2168f6b65a3f79f99e16d0e12156fd3', 1333, 1, 'user', '[]', 0, '2021-10-21 06:45:18', '2021-10-21 06:45:18', '2022-10-21 12:15:18'),
('a5405a5e486dac681c04d000165ec32d5d3b9376cfd1ee549f62766bf7781ad3c2e01e7f0cdb7f9b', 1229, 1, 'user', '[]', 0, '2021-04-12 06:49:47', '2021-04-12 06:49:47', '2022-04-12 12:19:47'),
('a694a9534497b020de669999374ac180d0d2f664c189a833bf30ba725d0636bda97ba515ab80d091', 1204, 1, 'user', '[]', 1, '2020-10-19 23:51:27', '2020-10-19 23:51:27', '2021-10-20 05:21:27'),
('a69a38ee49b28e0c05fdcfec5e210efec68e7f146e95818866dbce646c2b5fbc7f2a89e0dcaaef46', 1312, 1, 'user', '[]', 0, '2021-04-14 04:24:11', '2021-04-14 04:24:11', '2022-04-14 09:54:11'),
('a6a840e01b2c9df04e3c921ab82179e991724491bb19eb022f3b37b784d8078209880a48e259185c', 1333, 1, 'user', '[]', 0, '2021-10-21 06:34:54', '2021-10-21 06:34:54', '2022-10-21 12:04:54'),
('a6cb388565fe511c5919977d6bdb0a26798d5913a9610f5402fed7b368101f2fa4ea4e8856d6a297', 1195, 1, 'user', '[]', 1, '2020-02-07 01:08:28', '2020-02-07 01:08:28', '2021-02-07 06:38:28'),
('a737e03d8c2fd12ccd68f5734c4f23f451d1fe23dbbbb267b5679d86bad1905d3bf0d3a8bc267d08', 1265, 1, 'user', '[]', 0, '2021-04-09 02:55:15', '2021-04-09 02:55:15', '2022-04-09 08:25:15'),
('a7b77f408595a9ff62f8eb1afcff5ec4b0a50b2b372929745e2a94d2a1a38107a5a9ae3ab20147e8', 1311, 1, 'user', '[]', 1, '2021-04-14 04:07:54', '2021-04-14 04:07:54', '2022-04-14 09:37:54'),
('a7d69d2eb68f4428bb759ed57a641862a16e78166b8d12557a236d4d9fe1c5dedad46b70edb4a91a', 1192, 1, 'user', '[]', 0, '2020-02-17 01:34:49', '2020-02-17 01:34:49', '2021-02-17 07:04:49'),
('aaea56587197d2457eb08f45635f408bebd8b31fb5bde24b9fcd5a1e76bbe111784221a28a2e2008', 1193, 1, 'user', '[]', 0, '2021-04-06 06:36:21', '2021-04-06 06:36:21', '2022-04-06 12:06:21'),
('ab19a481994c5f15809b2349f900a3e071a815eff1fde4d99c722f2a0f7cdd76017912072af18def', 1329, 1, 'user', '[]', 1, '2021-10-21 04:41:21', '2021-10-21 04:41:21', '2022-10-21 10:11:21'),
('ab8463a44803d16538414b6d16caf5929190941eedda2feb14368d9abebcead5186f72ba281ab1a6', 1290, 1, 'user', '[]', 1, '2021-06-01 01:34:54', '2021-06-01 01:34:54', '2022-06-01 07:04:54'),
('abcb23c820ab3f8dd57ec13e4208d1b11cd9e6d8707bc2dcc31636593d230d23310c928dd0d64144', 1333, 1, 'user', '[]', 0, '2021-10-21 05:42:39', '2021-10-21 05:42:39', '2022-10-21 11:12:39'),
('acb3185fe734b8bc1466257c5897cb05e4bc4bd0c5b8a86ed866f3b8ccec2b3dce1eee5235de7b9d', 1344, 1, 'user', '[]', 0, '2021-11-01 06:33:55', '2021-11-01 06:33:55', '2022-11-01 12:03:55'),
('adf1b755e69defc0f84f15140f93cdbcf77e175a186a5724524dfd987b8e332f678fc27ef8a06fe1', 1193, 1, 'user', '[]', 0, '2020-10-19 22:57:42', '2020-10-19 22:57:42', '2021-10-20 04:27:42'),
('adf6e529b4939dcf2ed100c14bd0d6213b5be48c07ba7cf2c271ddd991e573d492bb321dfc3575c9', 1309, 1, 'user', '[]', 0, '2021-04-13 10:21:15', '2021-04-13 10:21:15', '2022-04-13 15:51:15'),
('aef8e791f2604fb49ad4060495edb432e7b0c6c8f982a17c14f5fe4408eebb8ac501c86d52583f7f', 1223, 1, 'user', '[]', 1, '2021-04-09 06:04:39', '2021-04-09 06:04:39', '2022-04-09 11:34:39'),
('af461623c7acbbcb4025f085b5ff401fdfbd3a28f1e269883664ec35bce8631a76e026bc3d9f7e55', 1280, 1, 'user', '[]', 0, '2021-04-12 07:52:54', '2021-04-12 07:52:54', '2022-04-12 13:22:54'),
('af64e6a39334816d47ac3f43e11add6e3660d16f0534c9be5930a9423137420fa17de3664499ec02', 1329, 1, 'user', '[]', 1, '2021-10-18 12:41:42', '2021-10-18 12:41:42', '2022-10-18 18:11:42'),
('afa81322d0854b6ba3ee1f27764c727b6220b521c06a3c5bad67bb38c9e2841f31acb0924741aeed', 1228, 1, 'user', '[]', 1, '2021-04-05 00:13:17', '2021-04-05 00:13:17', '2022-04-05 05:43:17'),
('afc4fa80406bf861cc07dab58d4d15d5f610b83adeb7c8f448b778887515c39964b0622cd0a88bf8', 1330, 1, 'user', '[]', 0, '2021-10-20 04:27:46', '2021-10-20 04:27:46', '2022-10-20 09:57:46'),
('b00300d73226b9cda7e4b7fb912a69ba23e9c97af55d53a831c4662a5ff7cde4a1d0c2341ca8feed', 1318, 1, 'user', '[]', 1, '2021-04-14 05:41:40', '2021-04-14 05:41:40', '2022-04-14 11:11:40'),
('b19e4b1140a0ff60689580af02bd05d7332b6f1fe72135f11fcd0bf3e6f24c869597d0e1c0809046', 1270, 1, 'user', '[]', 1, '2021-04-09 04:04:20', '2021-04-09 04:04:20', '2022-04-09 09:34:20'),
('b1bb64c24b20fae8fa09d36af352afe7c0b53be43d054834549a99c71690cade46422598f6c41e4e', 1342, 1, 'user', '[]', 1, '2021-10-28 06:15:54', '2021-10-28 06:15:54', '2022-10-28 11:45:54'),
('b1bc6b8573c3149abbf1dd23a9dcf9dc80cf8bdd7891c34b2047b1b1943f404827e6133d5688bf12', 1221, 1, 'user', '[]', 0, '2021-04-02 02:21:30', '2021-04-02 02:21:30', '2022-04-02 07:51:30'),
('b1f2e8bd876299478773be8fc5635723aa409c0c1afa126b96080c346e3d3a5649221c1fd1fe6c6d', 1302, 1, 'user', '[]', 1, '2021-04-14 02:06:48', '2021-04-14 02:06:48', '2022-04-14 07:36:48'),
('b2b4b68a123d1bc15584ed213ae2c77f3cf09066d105b831b2182867c915a222b9b2ae8546c11060', 1194, 1, 'user', '[]', 1, '2020-02-07 03:54:10', '2020-02-07 03:54:10', '2021-02-07 09:24:10'),
('b31f6be4e1caab08fb9d4d74371a29308fda0299ac362c8856eda17f9505486db9c771a00c1bf3d3', 1342, 1, 'user', '[]', 0, '2021-10-28 06:08:19', '2021-10-28 06:08:19', '2022-10-28 11:38:19'),
('b39eac4b83913764696557db488f56cde785ece6ffc569b6e9f93964223e241f9ae10a8626f7d95c', 1283, 1, 'user', '[]', 0, '2021-04-12 08:12:34', '2021-04-12 08:12:34', '2022-04-12 13:42:34'),
('b41e830465a285ef6d98d549599c67c7c4f55abbb82f6b0189dd281f0b122e56a184ecb7c43f10e6', 1296, 1, 'user', '[]', 0, '2021-04-12 22:48:03', '2021-04-12 22:48:03', '2022-04-13 04:18:03'),
('b49754082b92a88a3b4bbb2ff79bcc2cb881a214c6a784887c93cc121dac3aed40ee383626594ec4', 1229, 1, 'user', '[]', 1, '2021-04-05 00:25:28', '2021-04-05 00:25:28', '2022-04-05 05:55:28'),
('b4e7568d8767ed99be3b9ee2d1995e8fd7137eb8ddf64e8df72aecc5d3a9b3b536dc960a5a16a3a0', 1331, 1, 'user', '[]', 1, '2021-10-25 05:18:00', '2021-10-25 05:18:00', '2022-10-25 10:48:00'),
('b7dd6ad7d92f28c82ccc40d2ce200fbe26745bd616899d5ddf45f8fc19b93956b90ab7211495edab', 1339, 1, 'user', '[]', 1, '2021-10-28 05:31:54', '2021-10-28 05:31:54', '2022-10-28 11:01:54'),
('b7dd94611a9c96cf6a39219a1f385dc80d52f5ea155be5bbb97fe10ed585255565574095bf990ba8', 1193, 1, 'user', '[]', 0, '2021-03-31 00:03:42', '2021-03-31 00:03:42', '2022-03-31 05:33:42'),
('b7eddddca96a57389c711118799c457ce83aa1065b93e866d2fc04d6e5199fb91a80332f5d777f42', 1193, 1, 'user', '[]', 0, '2021-04-06 05:40:08', '2021-04-06 05:40:08', '2022-04-06 11:10:08'),
('b92b06e849b53e92a0e7317b13b0d090b61e592e39af6713daa5c58879a941efefe1af960cd21a2e', 1283, 1, 'user', '[]', 1, '2021-04-12 08:15:36', '2021-04-12 08:15:36', '2022-04-12 13:45:36'),
('b94551ee05417ddb94437db2a5374866acd2b90d94987d187b1cdca324b929a85c159b59eb7bdcaa', 1304, 1, 'user', '[]', 0, '2021-04-13 02:32:47', '2021-04-13 02:32:47', '2022-04-13 08:02:47'),
('ba3fa1e618b468bf3d2c643c2b13a365a46eb30ef62c6c18443626478bbfec2eda7d9d95a5a7cc21', 1324, 1, 'user', '[]', 1, '2021-04-21 07:12:20', '2021-04-21 07:12:20', '2022-04-21 12:42:20'),
('ba5afb6a9c0b125e04473ff05eb98d37278aa501068b94d84d8d862cf280b5ab071f4d3c852b24e9', 1319, 1, 'user', '[]', 1, '2021-04-14 06:03:14', '2021-04-14 06:03:14', '2022-04-14 11:33:14'),
('ba7e4e8906a8899b85543c4bf4f3f78cbdef6c89d037cc86579a90edf25dd54c12f76797519a3e3f', 1304, 1, 'user', '[]', 1, '2021-04-13 23:03:41', '2021-04-13 23:03:41', '2022-04-14 04:33:41'),
('ba9569a70c19b3c3b096913cd1bcb634a95b6e4ab1669937cfe4677e5718200f7f617b478b82235d', 1194, 1, 'user', '[]', 1, '2020-02-07 01:05:32', '2020-02-07 01:05:32', '2021-02-07 06:35:32'),
('bad8e5c64a7e72c6fa2761b4d5370b31bd84584b2b5f876abf57cf595764a4aee58b9bc7472956eb', 1343, 1, 'user', '[]', 0, '2021-10-28 06:24:46', '2021-10-28 06:24:46', '2022-10-28 11:54:46'),
('bb7f861964a944d8090d64e89d5c4fb10b0b0f3e68dd530bf2aa97b9fa7576aa59db3bea068a3394', 1222, 1, 'user', '[]', 0, '2021-03-31 03:14:38', '2021-03-31 03:14:38', '2022-03-31 08:44:38'),
('bb8ee2ee32ec56b944787913d768b7142f056e787f7ede03282c85668694b87a6bf1faaaa048c39a', 1316, 1, 'user', '[]', 0, '2021-04-14 05:03:02', '2021-04-14 05:03:02', '2022-04-14 10:33:02'),
('bbd75d229cff5b14abb5d0a6f11690a90a76d5b94f16829755cd11668f9e32d2e2c1f63065452aa2', 1199, 1, 'user', '[]', 0, '2020-02-11 07:08:59', '2020-02-11 07:08:59', '2021-02-11 12:38:59'),
('bbeac2729ce3b2250bcdbae74a25af0c9530b1ef7a2b1944faa93a3504175f7a78d12556c0d37026', 1290, 1, 'user', '[]', 0, '2021-04-20 06:00:41', '2021-04-20 06:00:41', '2022-04-20 11:30:41'),
('bc32bb80b6cfc0ef14a8c09c61d5e755058858adcec784b448ec5626b3653037ae561f048d8bc7b4', 1344, 1, 'user', '[]', 1, '2021-11-01 06:09:54', '2021-11-01 06:09:54', '2022-11-01 11:39:54'),
('bc4013db3dadb5218d86b9d806abb85076037e6b152e174beb4a1a821ad3f3524e7ad0dd991de4eb', 1212, 1, 'user', '[]', 0, '2021-01-11 23:39:50', '2021-01-11 23:39:50', '2022-01-12 05:09:50'),
('bc80f4153c7e5a7a0c6fba796b731a454baffa10b592aed7de9d0f1f2d48a6f401f87e16bec25639', 1308, 1, 'user', '[]', 0, '2021-04-13 03:47:26', '2021-04-13 03:47:26', '2022-04-13 09:17:26'),
('bc98b3adf1338c13cc451c44c4a54fd49e7d6bdd0d7bdcf07671b70e1267cd16256b9c9e78013cb6', 1204, 1, 'user', '[]', 0, '2020-10-19 23:50:15', '2020-10-19 23:50:15', '2021-10-20 05:20:15'),
('bce55bd3a127c241634376dc3422df734987f6d69797917e5c65833995c770ebfce4bff1d3605aee', 1193, 1, 'user', '[]', 1, '2021-04-19 05:34:29', '2021-04-19 05:34:29', '2022-04-19 11:04:29'),
('bce75e9a33c963417081778136a27bbe01810464ac58e48142a653912f67fd2b1b9cda0c0916d20d', 1333, 1, 'user', '[]', 0, '2021-10-21 05:02:31', '2021-10-21 05:02:31', '2022-10-21 10:32:31'),
('bced0a9fb9cc80365b828114e79710b2e58d6aff2e492340657906e899e7fa1f812358bd53948e2a', 1231, 1, 'user', '[]', 0, '2021-04-13 02:50:12', '2021-04-13 02:50:12', '2022-04-13 08:20:12'),
('bd6a86295824524f0d2eb88f730af23279469848004443944c2636f852e4d486b19916a31c220b3f', 1339, 1, 'user', '[]', 1, '2021-10-28 05:20:48', '2021-10-28 05:20:48', '2022-10-28 10:50:48'),
('be193c27d1be356fdd2b2523f14109c79fac591d5cb5883efa1e02044ebbe92c775417230d3237a1', 1343, 1, 'user', '[]', 1, '2021-10-28 07:38:56', '2021-10-28 07:38:56', '2022-10-28 13:08:56'),
('be675478a6a057b3d847f4bbcbc4367a77d4841f23c623931a64c63a9af93706c9348d439582604b', 1324, 1, 'user', '[]', 1, '2021-10-28 13:51:06', '2021-10-28 13:51:06', '2022-10-28 19:21:06'),
('be691e285fa5391666aecf261cf185243d7d063c7f1a419909322c602f6585e9f98c9bf25d7085cf', 1222, 1, 'user', '[]', 1, '2021-03-31 03:18:20', '2021-03-31 03:18:20', '2022-03-31 08:48:20'),
('be92ac02a8d66e7fcef054823e0622cc2e28b790dd13f8332a8870ceec603a4c8ca912e340d37be6', 1229, 1, 'user', '[]', 1, '2021-04-12 03:31:52', '2021-04-12 03:31:52', '2022-04-12 09:01:52'),
('bf74b65e49280b6596592ba1853991389449f305dd37ab11a306127e4a218ee895eebe656ddfba8b', 1304, 1, 'user', '[]', 0, '2021-04-13 01:57:23', '2021-04-13 01:57:23', '2022-04-13 07:27:23'),
('c1211960fafbab2ea3678a7f4127e81eb3136373d9f56fb4b9f2ec8afbf9e14bb95c40d5e31a360b', 1294, 1, 'user', '[]', 0, '2021-04-12 22:39:57', '2021-04-12 22:39:57', '2022-04-13 04:09:57'),
('c163824ddf8aa096b8a8f65051442b1bb5ec7f604fb9ee6c1266411a17c0815496db4fd7fe52cb1a', 1290, 1, 'user', '[]', 1, '2021-04-21 06:21:39', '2021-04-21 06:21:39', '2022-04-21 11:51:39'),
('c1d88b1d698dcf4c77699b21b26670f0962f18587ea4f3b1fb47e0d0d8cc20655c30a3bcf3ee427d', 1193, 1, 'user', '[]', 1, '2020-02-07 01:07:46', '2020-02-07 01:07:46', '2021-02-07 06:37:46'),
('c212278e62c678dfc59ac36b86c58975c2424db4a3db48e839ab16ab380d4c1c81ec8f340a1f2c19', 1321, 1, 'user', '[]', 1, '2021-10-27 05:57:36', '2021-10-27 05:57:36', '2022-10-27 11:27:36'),
('c24559674f99fb330ee82dd1ae61fc6ddb2c659f40fbd891063ca36198a84b54b0987291c85e7271', 1192, 1, 'user', '[]', 1, '2020-02-07 01:21:31', '2020-02-07 01:21:31', '2021-02-07 06:51:31'),
('c2522c7829f7137f1bb8f607f3256c274e58de2c027afd2cba0c06bffdef8599ba7bac185eefac99', 1282, 1, 'user', '[]', 0, '2021-04-12 08:07:09', '2021-04-12 08:07:09', '2022-04-12 13:37:09'),
('c320782f2d60fdbe334a8625e687225c43c74b53d638805228171859989d0ab323b93f81fea2b096', 1277, 1, 'user', '[]', 0, '2021-04-13 08:25:30', '2021-04-13 08:25:30', '2022-04-13 13:55:30'),
('c33c83abb5573c85029995baba5d3b0c610a0dd02b94f6df1bfa7f5a2b3d9f770074114ff00feaed', 1287, 1, 'user', '[]', 0, '2021-04-12 13:54:47', '2021-04-12 13:54:47', '2022-04-12 19:24:47'),
('c33f23cafec6f2570ca9d713cfd5e2648ea77d8c6a3747db081404cab5641c4b2e4367982dc10977', 1290, 1, 'user', '[]', 1, '2021-04-20 06:16:01', '2021-04-20 06:16:01', '2022-04-20 11:46:01'),
('c41a1e4402102a99aa371bdc01191235cb2566936408c22e3eb3c8c9ab59e6425f75ae67204ceab6', 1195, 1, 'user', '[]', 0, '2020-02-17 23:55:50', '2020-02-17 23:55:50', '2021-02-18 05:25:50'),
('c468d0ed8f3e3923f65a52393b6ac917fc601cdecc2d80462a18a4b775df150cc2fe93244430fb88', 1290, 1, 'user', '[]', 0, '2021-04-21 02:42:19', '2021-04-21 02:42:19', '2022-04-21 08:12:19'),
('c4a6d0df0e2e11905f3fa0814e9e0dbf76705a9ceefc06661c54788ab9a55a13cb92ea67c7b96a37', 1221, 1, 'user', '[]', 1, '2021-04-07 09:13:08', '2021-04-07 09:13:08', '2022-04-07 14:43:08'),
('c586aa4a4df83fe2abc01255f3ec85f7939259210bf37fe83af87061b43dba6a0903765b020ee3b8', 1274, 1, 'user', '[]', 1, '2021-04-09 05:14:18', '2021-04-09 05:14:18', '2022-04-09 10:44:18'),
('c5c95376b1cac6bcece72f1a1ae84673ff6cc3cc2c530659503c4406bfdf0e62f0fc612754dfe826', 1339, 1, 'user', '[]', 1, '2021-10-27 06:58:18', '2021-10-27 06:58:18', '2022-10-27 12:28:18'),
('c63778716b2ed5b356417cd9b9b1a4f0a22330c4b8826c47551a3347782935c212e5e17080d9e282', 1197, 1, 'user', '[]', 0, '2020-10-20 00:14:20', '2020-10-20 00:14:20', '2021-10-20 05:44:20'),
('c6979afd529e58aa551e8c372b12c9f4b7f10456372ce8f2c739df4abfa9577eb66e2ce246bf3b9a', 1221, 1, 'user', '[]', 1, '2021-03-31 01:38:35', '2021-03-31 01:38:35', '2022-03-31 07:08:35'),
('c74cd8f47beef540b0d4870475d1b074ff752b6ec6d5593d81f88897b5566cb4e533a0b9723b5aeb', 1336, 1, 'user', '[]', 0, '2021-10-26 05:42:39', '2021-10-26 05:42:39', '2022-10-26 11:12:39'),
('c7b4482374aec35cffd5a60426de1b86cd8ff9ce902204aac3092016d1afe7b4d1ca1a3c6c01398b', 1342, 1, 'user', '[]', 1, '2021-10-28 06:12:55', '2021-10-28 06:12:55', '2022-10-28 11:42:55'),
('c7ba7c8678e335ba86eb2231e861fb844f4fbe7969bec8fcfa88b60d5ab5fb50e2212d59d690b9e7', 1337, 1, 'user', '[]', 1, '2021-10-26 11:55:06', '2021-10-26 11:55:06', '2022-10-26 17:25:06'),
('c80dcdcb71fa68962e9e9e7172639ef192437686bd4e83d734301037dad163495cdfdb664fcef63f', 1272, 1, 'user', '[]', 1, '2021-04-09 04:45:44', '2021-04-09 04:45:44', '2022-04-09 10:15:44'),
('c82d298c757cda436338d8c9c98e8d97de7e07562d4bd49e9ad9f41082253040b3868c20686ca904', 1332, 1, 'user', '[]', 0, '2021-10-27 07:34:04', '2021-10-27 07:34:04', '2022-10-27 13:04:04'),
('c97e8ae34fcccbacbda92a4d76e2e8767bc203ab0c9995c4c7a0858b0942264cc3d9502e7922fc9b', 1262, 1, 'user', '[]', 1, '2021-04-09 02:19:03', '2021-04-09 02:19:03', '2022-04-09 07:49:03'),
('c9bc48c909ee322a584a8091202d3dff022b6b7c071916f3f45cf670bab4fcdebb39cf8861f45c03', 1333, 1, 'user', '[]', 0, '2021-10-21 22:50:40', '2021-10-21 22:50:40', '2022-10-22 04:20:40'),
('c9c93b0e3d9ee4e13a4e4ef0ed463e60b825b678e52282ce15186d05638575ece65c24f0e359e4bb', 1339, 1, 'user', '[]', 1, '2021-10-27 06:09:41', '2021-10-27 06:09:41', '2022-10-27 11:39:41'),
('ca6037588d08a888ad6a33d92b54881899dec2220670711633df131ddd5b539d7d00f3929b354d7a', 1221, 1, 'user', '[]', 1, '2021-04-05 02:32:08', '2021-04-05 02:32:08', '2022-04-05 08:02:08'),
('cae741113b10104daf47888ead2e5ca43c9479af412fcbc12e6c030f479d73c23860d9605ddfab27', 1202, 1, 'user', '[]', 0, '2020-02-17 03:59:27', '2020-02-17 03:59:27', '2021-02-17 09:29:27'),
('cbafcea54869b3c94420b5ad62a70048ea81fe2107b3425ab431b396924797b1e48686e26b06f63c', 1202, 1, 'user', '[]', 0, '2020-02-17 04:01:06', '2020-02-17 04:01:06', '2021-02-17 09:31:06'),
('cbf882504992873afcb0f3cc2b00575855384939b07e08924a14192bd6337656528528daf5d22363', 1220, 1, 'user', '[]', 0, '2021-03-31 01:21:54', '2021-03-31 01:21:54', '2022-03-31 06:51:54'),
('cbfe41f2c66b28cb16917af54001f75d96217e8848dfe6a9720129ebc0b65360e8416e8cc4b4b603', 1192, 1, 'user', '[]', 1, '2020-02-07 01:00:18', '2020-02-07 01:00:18', '2021-02-07 06:30:18'),
('cc196d2dc4d3cbcf4bdff0c682a9d214b433ca821ea8bd3b53a98f1ab936a0e66d2141abb23e1f9c', 1193, 1, 'user', '[]', 0, '2020-02-17 05:06:33', '2020-02-17 05:06:33', '2021-02-17 10:36:33'),
('cc93f7701fd0c93d1257fb178cc1091ab4a2574967b9d88f7c98c40d3ea50bf96172263d289cbff4', 1285, 1, 'user', '[]', 0, '2021-04-12 08:31:47', '2021-04-12 08:31:47', '2022-04-12 14:01:47'),
('cd01cf5c94b3ae25528cfb98e3a210e14680594baa8a05dd2708fca4a8c232b5b8f890193818ac99', 1339, 1, 'user', '[]', 1, '2021-10-27 07:33:30', '2021-10-27 07:33:30', '2022-10-27 13:03:30'),
('cd4f9e38d8829f3924450633ba3c0620778089847f5e73cae37f29c7e710edce710df372831cc9bf', 1255, 1, 'user', '[]', 0, '2021-04-07 23:58:46', '2021-04-07 23:58:46', '2022-04-08 05:28:46'),
('cd59e79475af970f9703fc27b7bc059cfb92998f97ca5e0b444c33704df31003a8452a145c7aafce', 1347, 1, 'user', '[]', 0, '2021-11-06 11:47:25', '2021-11-06 11:47:25', '2022-11-06 17:17:25'),
('cdc1911a368f98c91021ce924a846de3a5aa3ebc451fd644d357ef6aef610dffe342f8cb889ba3d9', 1342, 1, 'user', '[]', 1, '2021-10-28 07:49:34', '2021-10-28 07:49:34', '2022-10-28 13:19:34'),
('ce7e74de00af8cf9cc114500011d2605ea18ec6e7bef5ebc9ed268fcdf64609b39d7bfaa48cc1fac', 1199, 1, 'user', '[]', 0, '2020-02-11 07:05:57', '2020-02-11 07:05:57', '2021-02-11 12:35:57'),
('cf7aee9a2b206c10c9f4ce0b7f3a08bb8b16fac3aad58cd4866d2bf56e5612cee9c97c6962912813', 1332, 1, 'user', '[]', 1, '2021-10-27 06:01:19', '2021-10-27 06:01:19', '2022-10-27 11:31:19'),
('d03ee81c05a01fb230bcd8271652f1b8bf644aa5d68ebe870245ecd734df112a0725d568ab60bfef', 1212, 1, 'user', '[]', 1, '2021-01-11 23:42:22', '2021-01-11 23:42:22', '2022-01-12 05:12:22'),
('d0542ffacfb2dc825517db21375bfe16bb2e47525c1fd7b0fe8166c93b57f24ecff58a9b811c98e8', 1339, 1, 'user', '[]', 1, '2021-10-27 07:23:00', '2021-10-27 07:23:00', '2022-10-27 12:53:00'),
('d0d260521df2200f99e169241e84fb48a68d86345047ba9ff46d2a588d53ee34667f6be6fb389b71', 1220, 1, 'user', '[]', 1, '2021-03-31 23:58:35', '2021-03-31 23:58:35', '2022-04-01 05:28:35'),
('d0eaee4d7a7915a8c23d7c24ffbd56135b67f7919c19bb0ba16eb6c4fceab3bbd0b97e1acebf5265', 1343, 1, 'user', '[]', 1, '2021-10-28 07:05:21', '2021-10-28 07:05:21', '2022-10-28 12:35:21'),
('d0f56e1a1434b1084dc525f0e6a48938889ceb8516aae6cecdcdcfcbc2b7923b2ac3a5871b988bb2', 1271, 1, 'user', '[]', 0, '2021-04-09 04:10:34', '2021-04-09 04:10:34', '2022-04-09 09:40:34'),
('d0f6ea7f65a40567a50fb0b65dcd6a5215699801e2d0fe02248874733fcbe575c86df07393073ca3', 1318, 1, 'user', '[]', 0, '2021-04-14 05:36:11', '2021-04-14 05:36:11', '2022-04-14 11:06:11'),
('d164140c82ff58b5e7f863c4acf97bf9a26a69dd58615d4c2420a60ced0bff55c9e0fe2b23825a95', 1193, 1, 'user', '[]', 1, '2020-02-07 01:01:51', '2020-02-07 01:01:51', '2021-02-07 06:31:51'),
('d16594e6057acd154d2c8cd876fa87ccb83b782660c1b5fdbcc42f6a2335827b851b686e318433ba', 1326, 1, 'user', '[]', 0, '2021-10-07 01:20:08', '2021-10-07 01:20:08', '2022-10-07 06:50:08'),
('d16b66eb02582c89cb345337449b5e78debe363c07e4734cf8e749b70fb5c45393cb22c767ea853b', 1256, 1, 'user', '[]', 0, '2021-04-08 00:04:08', '2021-04-08 00:04:08', '2022-04-08 05:34:08'),
('d1b3240b761064691f6415274667d8b6740241cd099c76c1b7857ddcd811a5aef5b190b7839cc45d', 1346, 1, 'user', '[]', 0, '2021-11-02 01:08:16', '2021-11-02 01:08:16', '2022-11-02 06:38:16'),
('d207e556b1d19e35fab7f5d94db0fceaaad6e95dc481d4af3888c7c29861e2ceb3ce516e2667603e', 1272, 1, 'user', '[]', 0, '2021-04-09 04:34:41', '2021-04-09 04:34:41', '2022-04-09 10:04:41'),
('d2194935e4b62e394c57c393ef5a2272398e3a92ab60d00f7be63b8b7dee944cbfea66bc026f1dbb', 1229, 1, 'user', '[]', 1, '2021-04-07 06:16:46', '2021-04-07 06:16:46', '2022-04-07 11:46:46'),
('d21f57a341cdb3289c66c0dbf728dbca8bf20a002ace2ce4247d9970fd3560a812aadb7d783758aa', 1332, 1, 'user', '[]', 1, '2021-10-25 05:13:50', '2021-10-25 05:13:50', '2022-10-25 10:43:50'),
('d2f3f05cfd91e2466f9836fb6368c5187c199d698e57595b599847590504a4393f8556fcc6170492', 1333, 1, 'user', '[]', 0, '2021-10-21 06:40:05', '2021-10-21 06:40:05', '2022-10-21 12:10:05'),
('d40b252aa469ca465e5d41d9a7b7fa5ffef859683aff2d005d97e178190b0c4aee4d10305f994d66', 1344, 1, 'user', '[]', 1, '2021-11-01 06:01:28', '2021-11-01 06:01:28', '2022-11-01 11:31:28'),
('d50874774337ea68fa6246298b5c10c545c4e98672b3e75158d4ad294686c1e7fcfb4e5e5e48d840', 1221, 1, 'user', '[]', 0, '2021-04-07 06:13:46', '2021-04-07 06:13:46', '2022-04-07 11:43:46'),
('d577f1c92579b309ef3ae972930e3ec75cdd28d43a9b26f80f4cb01c18eac8d2618fb9c697188237', 1333, 1, 'user', '[]', 0, '2021-10-21 06:56:49', '2021-10-21 06:56:49', '2022-10-21 12:26:49'),
('d58d0f809d9085d12885539edab26409a0c18c1b429c130c4856dc8c61fe4458be36aa5045f490d9', 1342, 1, 'user', '[]', 1, '2021-10-28 07:22:44', '2021-10-28 07:22:44', '2022-10-28 12:52:44'),
('d590e2bd2a5a4440a5006cb852effa9e2d596893d17cf83b3bf52079f0194a0043b9a63aace571e9', 1233, 1, 'user', '[]', 0, '2021-04-06 00:22:10', '2021-04-06 00:22:10', '2022-04-06 05:52:10'),
('d5aab30b617bb11a5844fc017168c723eec0c82a771f2926c5140e7ec521da0531f4f997cd2524e3', 1193, 1, 'user', '[]', 0, '2021-04-06 03:56:20', '2021-04-06 03:56:20', '2022-04-06 09:26:20'),
('d5e948e88941faf79519a1509a15b9bd40c85fbfc94b9f3dc821b9a7c64bae0b954d937cfe33d4a2', 1292, 1, 'user', '[]', 1, '2021-04-12 22:33:49', '2021-04-12 22:33:49', '2022-04-13 04:03:49'),
('d5f0740f22bc738941f30bfda3607e535a05885f97dcfea17d384ae7e0104384c1e01cb4b4713122', 1263, 1, 'user', '[]', 1, '2021-04-09 02:43:39', '2021-04-09 02:43:39', '2022-04-09 08:13:39'),
('d673d884e2ceb81894276e00b50da8fda781a42de8dd122e610c14ce506846a7c75013f6727c59a0', 1290, 1, 'user', '[]', 1, '2021-04-21 07:09:02', '2021-04-21 07:09:02', '2022-04-21 12:39:02'),
('d7a8c37503db82f54f043804e3082185b85d5e429cbcf50f735e84bb3ecc7c4997edc1f528a1bcb2', 1192, 1, 'user', '[]', 0, '2020-02-07 00:46:51', '2020-02-07 00:46:51', '2021-02-07 06:16:51'),
('d7aa48b1cba92e954b54cd61bcabfb1ad7333e430e757a17e899dddc4185e4028834f552991dce55', 1203, 1, 'user', '[]', 0, '2020-10-19 23:28:45', '2020-10-19 23:28:45', '2021-10-20 04:58:45'),
('d81501be857523b879782e8756ecf2003547b1702503e3de7727c6506f3ca200b09464b41bae99cd', 1209, 1, 'user', '[]', 0, '2020-10-25 12:39:09', '2020-10-25 12:39:09', '2021-10-25 18:09:09'),
('d8d3f8668325b9b82ec5b9f9db12a1864e329f3e75fd79835263be699cb03b01bc44c1caacd03b88', 1197, 1, 'user', '[]', 1, '2020-02-07 04:55:22', '2020-02-07 04:55:22', '2021-02-07 10:25:22'),
('d96bbc96a9343f89265de36765765546292ab65d94c8ec3023ec750ce576f02a7c86b713d32e6e88', 1214, 1, 'user', '[]', 0, '2021-03-30 07:33:43', '2021-03-30 07:33:43', '2022-03-30 13:03:43'),
('d9b0472d782c4679bbe1509ff9ee46eff8faf7a34f3ba083d7f26af9cce981e66db58742c805e0e9', 1339, 1, 'user', '[]', 0, '2021-10-27 06:08:02', '2021-10-27 06:08:02', '2022-10-27 11:38:02'),
('d9f1fb7142affb6703f7f1988b1d986012df35b1d49d54c96d399c2413ba941fd79ce444b1136902', 1282, 1, 'user', '[]', 1, '2021-04-14 02:12:52', '2021-04-14 02:12:52', '2022-04-14 07:42:52'),
('dadebdd4adbb3be7c04d0b42f0a6f9946263da2a4ebc1ab9048b1e031857308700c672afe79f52cc', 1220, 1, 'user', '[]', 1, '2021-03-31 01:28:57', '2021-03-31 01:28:57', '2022-03-31 06:58:57'),
('db5b21a94d36b7b0188cfa72514af0b73a3407c0726a677a334412ffb2531601e458c711e78ebcb2', 1193, 1, 'user', '[]', 1, '2020-02-07 01:06:16', '2020-02-07 01:06:16', '2021-02-07 06:36:16'),
('db65edd15cc3cdb9ac3320eb36797c683fdb2946e82fbf87861708bce8f87cce37e8ee6b50559acd', 1321, 1, 'user', '[]', 0, '2021-04-19 00:16:15', '2021-04-19 00:16:15', '2022-04-19 05:46:15'),
('dcc04a04941228e72414f6e007b185a973c1b914ddca97d1d45d14e937c1bcb829a04fcb495920e5', 1197, 1, 'user', '[]', 0, '2020-02-07 04:54:19', '2020-02-07 04:54:19', '2021-02-07 10:24:19'),
('dcc4706aef474324e7678a10e480c705871f01457b873e07a9328a7bf7f129c3a80f836f050aa7e2', 1263, 1, 'user', '[]', 0, '2021-04-09 02:42:31', '2021-04-09 02:42:31', '2022-04-09 08:12:31'),
('dd3a9ee3e2df73f7c383a70df1e83306efa81444774349cc311a8ef28fe45e09ff85872d3f28aeda', 1221, 1, 'user', '[]', 1, '2021-04-02 00:36:41', '2021-04-02 00:36:41', '2022-04-02 06:06:41'),
('dd9e3f14f97f09c5dd0d46da3f4ce67c86349e335582e76493fe511e911c4aaaae648f52fe403c68', 1250, 1, 'user', '[]', 0, '2021-04-07 08:40:12', '2021-04-07 08:40:12', '2022-04-07 14:10:12'),
('ddadf1a0b378829405a3615bae982973bf695d6ddea1e76550156d4004ac7fdbf5bd3c950a991541', 1304, 1, 'user', '[]', 1, '2021-04-13 05:47:52', '2021-04-13 05:47:52', '2022-04-13 11:17:52'),
('dde78b03fadcc3cce2da0b5e9523ff051e4f4e7b3f587ae7e5cfa0a6feb6dad9c1ff8095c318b628', 1322, 1, 'user', '[]', 0, '2021-04-19 06:29:56', '2021-04-19 06:29:56', '2022-04-19 11:59:56'),
('dfa5b7a0fedeb15835fc90a1cc4f58295645df549f9811602889aecd439eb95aeb39634f2ccedcf6', 1195, 1, 'user', '[]', 1, '2020-02-07 01:14:33', '2020-02-07 01:14:33', '2021-02-07 06:44:33'),
('dfb99099c190920a3f32b040d83cb603b35a36f336211b3fdc736117a8fdec9860e9b442714d3da6', 1320, 1, 'user', '[]', 0, '2021-04-14 06:44:44', '2021-04-14 06:44:44', '2022-04-14 12:14:44'),
('dfd9b01929f3a4f60656fea0298e69373b6ade47eb2748c55f0c339618eef3615f22b7b0b456f58a', 1293, 1, 'user', '[]', 0, '2021-04-12 22:36:03', '2021-04-12 22:36:03', '2022-04-13 04:06:03'),
('e03f28603ec78108dc7245894dd34591b705a160aa4c1f2e0877fbc4e6ae21705797d9cf4242bc71', 1341, 1, 'user', '[]', 0, '2021-10-28 05:50:47', '2021-10-28 05:50:47', '2022-10-28 11:20:47'),
('e0452ad939e1db3852d7e95e1d8785121c4b06209dbb7127e63dfb9035a3a7d99107127370267e4b', 1231, 1, 'user', '[]', 0, '2021-04-13 03:04:36', '2021-04-13 03:04:36', '2022-04-13 08:34:36'),
('e05d27ca54b5da36357e140d16bd11e38f9963a4d7e37037e0148ca79d6325feab8914f6e14c72dd', 1332, 1, 'user', '[]', 0, '2021-10-25 05:12:36', '2021-10-25 05:12:36', '2022-10-25 10:42:36'),
('e1468a59828db2e8e7e97a9e33db72c8cff510a5f504a5101545eab453805f7fdca640e017227b95', 1315, 1, 'user', '[]', 0, '2021-04-14 04:49:25', '2021-04-14 04:49:25', '2022-04-14 10:19:25'),
('e168a64972989d734da6af789f9417b329f732e85a53acb06aad7054444702f907e555c1571ab437', 1220, 1, 'user', '[]', 0, '2021-03-31 01:21:24', '2021-03-31 01:21:24', '2022-03-31 06:51:24'),
('e18618db19e72a6e0f0b033fa3a5e8afb3cc52cb4c566868ea497f1e8c3b2731b1723c35f0a4650b', 1193, 1, 'user', '[]', 1, '2021-04-15 08:36:22', '2021-04-15 08:36:22', '2022-04-15 14:06:22'),
('e1f88573d36052b1a446acc2d3b9117b83923f00145e9095aea1aae1478bc17283f360b96c82201c', 1325, 1, 'user', '[]', 0, '2021-10-06 06:24:41', '2021-10-06 06:24:41', '2022-10-06 11:54:41'),
('e292be8c019079c0155e28e4b637bd52c8e7417b5cf359871e86fb9f4a351a8d5dc287be61f6d583', 1323, 1, 'user', '[]', 1, '2021-04-20 05:14:11', '2021-04-20 05:14:11', '2022-04-20 10:44:11'),
('e32f0c9f53ea5af358f907752bc620676b077a4cee62284a56b08facc748a06721b397bfb2cdd15a', 1290, 1, 'user', '[]', 0, '2021-04-21 02:42:11', '2021-04-21 02:42:11', '2022-04-21 08:12:11'),
('e33e2d65b6230fafb1e4076e0b22ccd3a8c0c742a3244b70d4c46843292ba22eb207e7881dbd15df', 1193, 1, 'user', '[]', 1, '2021-04-06 08:29:34', '2021-04-06 08:29:34', '2022-04-06 13:59:34'),
('e3c32460697467dd8914a0118c88840d2cff1c17a9b1cd61a0e03487794f4e4d0e05497f6bac77db', 1333, 1, 'user', '[]', 0, '2021-10-21 05:56:39', '2021-10-21 05:56:39', '2022-10-21 11:26:39'),
('e465bf21cb7f4b82a11a31c10691d9dede0b983ba0e911defffb84ef0ece05fceb99b9236999146f', 1339, 1, 'user', '[]', 1, '2021-10-28 05:13:54', '2021-10-28 05:13:54', '2022-10-28 10:43:54'),
('e57e1c66e530b32b94bfb671c1c6bac1df386961b1dd545b1843761eecb4ba8c188f63f07eeb518a', 1260, 1, 'user', '[]', 0, '2021-04-08 23:38:10', '2021-04-08 23:38:10', '2022-04-09 05:08:10'),
('e6e4d3d646e59f7668f9690c2bb04faeca87cf22546a0af5d7a4bfefeae38f944699e7672835bb17', 1193, 1, 'user', '[]', 1, '2021-04-06 03:57:37', '2021-04-06 03:57:37', '2022-04-06 09:27:37'),
('e6ec2822a66d7a5ee2d5dcf562a26dfbedda1f6b256ebc2023ac23f63a43ba6f0f72105226995f85', 1213, 1, 'user', '[]', 0, '2021-01-12 00:21:06', '2021-01-12 00:21:06', '2022-01-12 05:51:06'),
('e797ecd2cc397f0cc6ca246ba5522168f306f99b517999605243ac029f331c6e94620eece4c57064', 1231, 1, 'user', '[]', 0, '2021-04-08 00:42:04', '2021-04-08 00:42:04', '2022-04-08 06:12:04'),
('e86c854f78383b48ac2995d9390b7091670e2cc55012afdd135f641b9e460f7f9895d27497a455ea', 1220, 1, 'user', '[]', 1, '2021-03-31 01:27:04', '2021-03-31 01:27:04', '2022-03-31 06:57:04'),
('e907253739472e490de497388677ddd737c9e79cd8a0b593f587a50d160826247cf31288d0263ce6', 1252, 1, 'user', '[]', 1, '2021-04-14 01:00:33', '2021-04-14 01:00:33', '2022-04-14 06:30:33'),
('e918334e3124fffc8e8c03847713e082704fb87bba91bc01996e524c53d73737d3e9f9670abae321', 1247, 1, 'user', '[]', 0, '2021-04-07 08:12:05', '2021-04-07 08:12:05', '2022-04-07 13:42:05'),
('ea1472e17decdf8024d0c568ed2f001c5b9c413742c61c79d37f0b5a9270c0962cabfc19e059ebd4', 1333, 1, 'user', '[]', 0, '2021-10-21 05:14:10', '2021-10-21 05:14:10', '2022-10-21 10:44:10'),
('eade53c39aa240baf8f12dae0589780f285525597f3c91e747d37ff62f0dcef8fd96116d8e65905b', 1333, 1, 'user', '[]', 0, '2021-10-21 05:45:26', '2021-10-21 05:45:26', '2022-10-21 11:15:26'),
('eb80df208b600ee5fce9369350605439e7f1c65003a4dbe0f750b3b34cbc52323b903d5417254010', 1234, 1, 'user', '[]', 0, '2021-04-06 00:51:54', '2021-04-06 00:51:54', '2022-04-06 06:21:54'),
('eb8fb1659f798fb1d91f3c92ad3884f6615c6a72d96108867f4bd1ff93da477927c3ef91d42a417c', 1305, 1, 'user', '[]', 0, '2021-04-13 02:36:08', '2021-04-13 02:36:08', '2022-04-13 08:06:08'),
('ec3f82b9dd7483270c79e4a625513cfdd3e3f7ffe7c0710ae6b3107023ba6f8ad917dd225c6fcfdd', 1194, 1, 'user', '[]', 1, '2020-02-07 04:08:14', '2020-02-07 04:08:14', '2021-02-07 09:38:14'),
('ec435b3dae0c212c1d50ac183c5421bf9b858a223e98328217bb9acc6a05764912ac0930858f0e3d', 1301, 1, 'user', '[]', 0, '2021-04-12 23:51:55', '2021-04-12 23:51:55', '2022-04-13 05:21:55'),
('eca683254ad66e833bc7a20293e14cb19115851b10dcd5d2eecafce1210e1960edc82e94976e4bbb', 1193, 1, 'user', '[]', 0, '2021-04-06 03:55:42', '2021-04-06 03:55:42', '2022-04-06 09:25:42'),
('ecab46a8f66479f27b8e7dfd7cd2d561d6a472865ff4aeeae1f9d25b8df7251e6d2c56a5f2028852', 1333, 1, 'user', '[]', 1, '2021-10-21 07:13:34', '2021-10-21 07:13:34', '2022-10-21 12:43:34'),
('ecfb68efa2c6245d3e5eb3467d7b4155776f6f9343852ea82104c93417f96d556bc2388a6d7ddc48', 1223, 1, 'user', '[]', 0, '2021-04-01 01:13:21', '2021-04-01 01:13:21', '2022-04-01 06:43:21'),
('ed7c3297b963cfc8dbd152b98b7e865e433956b3c74df2bd6476c626aa29ac14457aa12b4d1a0128', 1223, 1, 'user', '[]', 1, '2021-04-13 07:35:40', '2021-04-13 07:35:40', '2022-04-13 13:05:40'),
('eda2c523ac83991b39de6b2072e8260560ce26e58524a69fe282a74f1f6319ed71b682c65ad6280e', 1290, 1, 'user', '[]', 1, '2021-04-21 04:41:58', '2021-04-21 04:41:58', '2022-04-21 10:11:58'),
('edba92342833aa25d6b251e53a36cda24849532bbceaf314c4e18dd52cd459fe931840229128739a', 1221, 1, 'user', '[]', 1, '2021-04-02 06:52:40', '2021-04-02 06:52:40', '2022-04-02 12:22:40'),
('ee918926ed6171468720ddfb54878dc75a6cb5b0993c33f8761818185b965a8a0225578f326973e2', 1342, 1, 'user', '[]', 1, '2021-10-28 07:35:13', '2021-10-28 07:35:13', '2022-10-28 13:05:13'),
('eeb47930ceb603b10a3d7e9bdc9f2a042e985873592ec474c106c96c5cee60177a2224c32a2fde85', 1309, 1, 'user', '[]', 0, '2021-04-13 10:23:25', '2021-04-13 10:23:25', '2022-04-13 15:53:25'),
('f0343b725e493d162dd8ab5f651a65d31288159b34fa2daee017623579bf0f11506ab05ef6fa082d', 1193, 1, 'user', '[]', 1, '2020-02-07 00:50:17', '2020-02-07 00:50:17', '2021-02-07 06:20:17'),
('f04a44ff2ea9881da280c63a3a406a15374f4a7eb65ecd86ec51800c543fa761f8c318eb073cb08d', 1313, 1, 'user', '[]', 0, '2021-04-14 04:28:30', '2021-04-14 04:28:30', '2022-04-14 09:58:30'),
('f09b5dbcea1f4030240a4a4289228c426155ee5ccb0573fbeadf3488b1b3b749315fe909855a4ea1', 1229, 1, 'user', '[]', 0, '2021-04-05 00:22:39', '2021-04-05 00:22:39', '2022-04-05 05:52:39'),
('f1b3404cdca23bbe29ff83f82e3191de4eabf99206c797e020bd35a779937235b0970009ae4d144d', 1342, 1, 'user', '[]', 1, '2021-10-28 06:09:10', '2021-10-28 06:09:10', '2022-10-28 11:39:10'),
('f21a22219ba1e82f10661ac7742bf48329b6902681fee538588201fd7fa69d84b570f36e2d4c800f', 1310, 1, 'user', '[]', 1, '2021-04-14 04:05:58', '2021-04-14 04:05:58', '2022-04-14 09:35:58'),
('f2d1e1109da5d59257e5ad8d770c1065f1174f49e33cd05480212adc466f5eb033d6720d27b68b8f', 1193, 1, 'user', '[]', 1, '2020-02-07 01:11:53', '2020-02-07 01:11:53', '2021-02-07 06:41:53'),
('f2d27b3bebb9cd3bc574a63c430b5bbad9bd48eeeef034e9065cba08f12866e0e463a526126f10e4', 1331, 1, 'user', '[]', 1, '2021-10-25 05:11:35', '2021-10-25 05:11:35', '2022-10-25 10:41:35'),
('f2e8240bb71ccd30bed9aac172bebf1476abe2bf2cb0a111ba47d8ad77066bf7a6a415750e83ecc9', 1343, 1, 'user', '[]', 0, '2021-10-28 07:52:19', '2021-10-28 07:52:19', '2022-10-28 13:22:19'),
('f3ee827f0d5961c701da1cd84fe09d23ac5b36485f707889b996af2a865da55dc277d121af00f671', 1329, 1, 'user', '[]', 0, '2021-10-18 12:37:54', '2021-10-18 12:37:54', '2022-10-18 18:07:54'),
('f49f6e869947fce479ff88cbeec9df6686659ef0323f3fed2f28071b8bcf4ff227b3dd5ad74cfa7d', 1294, 1, 'user', '[]', 0, '2021-04-12 22:43:36', '2021-04-12 22:43:36', '2022-04-13 04:13:36'),
('f4d1b775744618976cc70fc2a6cb8cdb1d63d818028ad703192eb2876d213d9e8a3e5399383d6137', 1243, 1, 'user', '[]', 1, '2021-04-21 07:05:37', '2021-04-21 07:05:37', '2022-04-21 12:35:37'),
('f4e3c07278ce12edad6a9e06b14cc50d67507238aa4fa75dd7b867d715d4e4c0773a6720b91a0b79', 1332, 1, 'user', '[]', 1, '2021-10-25 05:14:26', '2021-10-25 05:14:26', '2022-10-25 10:44:26'),
('f655f243526922860ea6044b858c3fe33b2c1a09d94ccbb08d2692c7b4281c5c17b0fcaace3fc3d0', 1203, 1, 'user', '[]', 0, '2020-10-19 23:25:40', '2020-10-19 23:25:40', '2021-10-20 04:55:40'),
('f65b478cd1e321be462563752d74e3b825a6a984fd05c40759ef825aaed1db4fca1020b385f805c1', 1342, 1, 'user', '[]', 1, '2021-10-28 07:08:05', '2021-10-28 07:08:05', '2022-10-28 12:38:05'),
('f66ea9fd98911cffdb77204fb1883b25a3c0ec938d1591befd895354980c8ae83f32ba397db9a3c3', 1333, 1, 'user', '[]', 1, '2021-10-21 07:35:29', '2021-10-21 07:35:29', '2022-10-21 13:05:29'),
('f71e88ce4378d7f91a8c3af7d63b2d18fb3564825859723ffb662a5d1fe24d8dde55f902faebc43a', 1272, 1, 'user', '[]', 1, '2021-04-09 04:35:34', '2021-04-09 04:35:34', '2022-04-09 10:05:34'),
('f77f4b3033547e4235d2953c946859f2ffdbe6ad6d1a0f33999e2c9fdf1355b9f5053c1295d4b8ad', 1333, 1, 'user', '[]', 0, '2021-10-21 06:48:21', '2021-10-21 06:48:21', '2022-10-21 12:18:21'),
('f89e428fa35fd21385892fb057a599f778b7cd2e4cc2a2ac42d9c297a7e8a77456b4fb878c900316', 1333, 1, 'user', '[]', 0, '2021-10-21 06:55:00', '2021-10-21 06:55:00', '2022-10-21 12:25:00'),
('f8f0da0d222e77dcd8cd226e0d7dc38e59cb1ffa3b2c251a121d00c10379f9910d661470c1697055', 1342, 1, 'user', '[]', 1, '2021-10-28 06:10:35', '2021-10-28 06:10:35', '2022-10-28 11:40:35'),
('f912d59a8193879ada4933320e1748ca8300c933f24865f15ff00479e24831e5927a6a08b2e51a7d', 1338, 1, 'user', '[]', 1, '2021-10-28 13:56:53', '2021-10-28 13:56:53', '2022-10-28 19:26:53'),
('f972148f4ce5826c7e7e09c8aa6a7c232dec2e5922958e5cc71cd14405090db55d7645802b5f4711', 1229, 1, 'user', '[]', 1, '2021-04-12 07:36:40', '2021-04-12 07:36:40', '2022-04-12 13:06:40'),
('f98bd444db9ab2c1395483c8dc91511fb7b80669012f539b5210f25280c37b6b5ad8e9aaf5d7bd52', 1275, 1, 'user', '[]', 1, '2021-04-09 05:26:24', '2021-04-09 05:26:24', '2022-04-09 10:56:24'),
('f9b30367b0b008f092a0c58328be37e0021b43abd8a3597ae5dc5854a1560a2af1f208d6c78ad068', 1330, 1, 'user', '[]', 1, '2021-10-20 04:29:55', '2021-10-20 04:29:55', '2022-10-20 09:59:55'),
('fa02fcca370463a3569b7cfffe71c49cb8d3d21bd6367403a87f57129d830d524d637014fce77239', 1244, 1, 'user', '[]', 0, '2021-04-07 05:30:03', '2021-04-07 05:30:03', '2022-04-07 11:00:03'),
('fa03945faefa101eee84e6bde6d266adf2706793097686af4ed93f09611736e3fea4dd566314d6aa', 1333, 1, 'user', '[]', 0, '2021-10-21 04:46:30', '2021-10-21 04:46:30', '2022-10-21 10:16:30'),
('fa475c5d3e12a3f0af612db1ab5aa1b886a8afe12e37ed0ba3e50e2574d60204dd5d7484b8897bcb', 1303, 1, 'user', '[]', 0, '2021-04-13 00:00:08', '2021-04-13 00:00:08', '2022-04-13 05:30:08'),
('fb2d82f98828605a036fce66eb41d5522de4fbca0bccd3c2a78e76ef1568817c56ee9bb1d0b12bb5', 1337, 1, 'user', '[]', 0, '2021-10-26 11:53:50', '2021-10-26 11:53:50', '2022-10-26 17:23:50'),
('fbcc2a76e5f1fec0438826c3c175bee2f3d80ebc14ef2d6f148038cc3303f3dc376a9df9f2295490', 1331, 1, 'user', '[]', 0, '2021-10-20 04:41:33', '2021-10-20 04:41:33', '2022-10-20 10:11:33'),
('fc38918ca6fb2740ee30f289e7692885923d2f5d6f60566d523a567de4273d1565f65d868627f38a', 1278, 1, 'user', '[]', 0, '2021-04-12 05:07:58', '2021-04-12 05:07:58', '2022-04-12 10:37:58'),
('fcaea5728046f7735c004094ff7af0db071eb6d5f80bbd7c0ee30a3388e0b7f652affe2a3e0f4ac4', 1221, 1, 'user', '[]', 1, '2021-04-07 06:14:33', '2021-04-07 06:14:33', '2022-04-07 11:44:33'),
('fd423702fccbd5363f04a815cea59b5dda53ae7848dfe0a15e6b74f891d14d5a3f3697b85d76f835', 1252, 1, 'user', '[]', 1, '2021-04-08 05:48:01', '2021-04-08 05:48:01', '2022-04-08 11:18:01'),
('fdcd70d9580485d02275f8f1ac604a7e06bae431992f173cf6fd65a8c4f1fbffa257d7cc18aecd8b', 1252, 1, 'user', '[]', 0, '2021-04-07 22:58:27', '2021-04-07 22:58:27', '2022-04-08 04:28:27'),
('fde51063ac15c2236eda54a1ddaf9fc74587424c02ff9097cc8048ee6737d45a4edc970242c389d3', 1304, 1, 'user', '[]', 0, '2021-04-13 01:52:31', '2021-04-13 01:52:31', '2022-04-13 07:22:31'),
('fe3a3399373c97a81b3cc51118e42af4827cd97ee2c12ac8270c507bd82c44f043f84c96f4d4b079', 1243, 1, 'user', '[]', 0, '2021-04-12 15:39:16', '2021-04-12 15:39:16', '2022-04-12 21:09:16'),
('fea3615bb3f0e62d151a8127ea050cd3bec572486e1b8a9f2e05f32af43ab84e6e480dcca9eb5954', 1211, 1, 'user', '[]', 0, '2021-03-31 00:22:46', '2021-03-31 00:22:46', '2022-03-31 05:52:46'),
('fede54a872f2df3b6c8fb5fe5c56bcd4d6306cb54a446124de3059349a88d6929e3a7c84775b5524', 1279, 1, 'user', '[]', 1, '2021-04-12 07:39:15', '2021-04-12 07:39:15', '2022-04-12 13:09:15'),
('fee105e119443ea3fdf429fa8b8ab23d389af9082510c8991fa7bad84b4b1e7229f27a4b46fc2848', 1193, 1, 'user', '[]', 0, '2021-04-06 03:56:26', '2021-04-06 03:56:26', '2022-04-06 09:26:26'),
('ff0da88c7657c66d53fb396aa5b22b0e6ed22b79a4b6b3807da393a27a6aa2e4f6f6ebbb931af96c', 1220, 1, 'user', '[]', 1, '2021-03-31 02:37:40', '2021-03-31 02:37:40', '2022-03-31 08:07:40');

-- --------------------------------------------------------

--
-- Table structure for table `oauth_auth_codes`
--

CREATE TABLE `oauth_auth_codes` (
  `id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  `client_id` int(10) UNSIGNED NOT NULL,
  `scopes` text COLLATE utf8mb4_unicode_ci,
  `revoked` tinyint(1) NOT NULL,
  `expires_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `oauth_clients`
--

CREATE TABLE `oauth_clients` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `secret` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `redirect` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `personal_access_client` tinyint(1) NOT NULL,
  `password_client` tinyint(1) NOT NULL,
  `revoked` tinyint(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `oauth_clients`
--

INSERT INTO `oauth_clients` (`id`, `user_id`, `name`, `secret`, `redirect`, `personal_access_client`, `password_client`, `revoked`, `created_at`, `updated_at`) VALUES
(1, NULL, 'Love Push Personal Access Client', 'Abxmt0kiTkWqZT8Bg9s3BdqoGBlkq2T2nRS3tTrm', 'http://localhost', 1, 0, 0, '2019-01-21 04:55:50', '2019-01-21 04:55:50');

-- --------------------------------------------------------

--
-- Table structure for table `oauth_personal_access_clients`
--

CREATE TABLE `oauth_personal_access_clients` (
  `id` int(10) UNSIGNED NOT NULL,
  `client_id` int(10) UNSIGNED NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `oauth_personal_access_clients`
--

INSERT INTO `oauth_personal_access_clients` (`id`, `client_id`, `created_at`, `updated_at`) VALUES
(1, 1, '2019-01-21 04:55:50', '2019-01-21 04:55:50');

-- --------------------------------------------------------

--
-- Table structure for table `oauth_refresh_tokens`
--

CREATE TABLE `oauth_refresh_tokens` (
  `id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `access_token_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `revoked` tinyint(1) NOT NULL,
  `expires_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `password_resets`
--

CREATE TABLE `password_resets` (
  `email` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `paypal_trans`
--

CREATE TABLE `paypal_trans` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `plan_id` int(11) NOT NULL,
  `plan_started_at` varchar(255) NOT NULL,
  `paypal_token` varchar(255) NOT NULL,
  `paypal_trans_id` varchar(255) DEFAULT NULL,
  `plan_cancelled_at` varchar(255) DEFAULT NULL,
  `plan_status` enum('0','1') NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `plans`
--

CREATE TABLE `plans` (
  `id` int(11) NOT NULL,
  `plan_id` varchar(255) NOT NULL,
  `type` enum('FREE','PREMIUM','VIP') NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` float(15,2) NOT NULL,
  `messages` varchar(255) NOT NULL COMMENT 'per day',
  `sending_requests` varchar(255) NOT NULL COMMENT 'per day',
  `see_love_requests` varchar(255) NOT NULL COMMENT 'per day',
  `recommended_profiles` varchar(255) NOT NULL COMMENT 'per week',
  `ads` varchar(255) NOT NULL COMMENT 'per month',
  `status` enum('0','1') NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `plans`
--

INSERT INTO `plans` (`id`, `plan_id`, `type`, `name`, `price`, `messages`, `sending_requests`, `see_love_requests`, `recommended_profiles`, `ads`, `status`, `created_at`, `updated_at`) VALUES
(1, '1', 'FREE', 'Free', 0.00, 'Unlimited', 'Unlimited', 'Unlimited', 'Unlimited', 'Unlimited', '1', '2019-12-31 11:21:33', '2021-04-01 00:48:59'),
(3, 'P-6K8439611U726951VP3TICLA', 'VIP', 'V.I.P', 9.90, 'Unlimited', 'Unlimited', 'Unlimited', 'Unlimited', 'Unlimited', '1', '2019-12-31 11:26:43', '2021-04-01 01:32:45');

-- --------------------------------------------------------

--
-- Table structure for table `posts`
--

CREATE TABLE `posts` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `title` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `image` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_post_anonymously` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1=> show, 0=> profile hidden',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1=> active, 0=> not active',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `posts`
--

INSERT INTO `posts` (`id`, `user_id`, `title`, `description`, `image`, `is_post_anonymously`, `status`, `created_at`, `updated_at`) VALUES
(20, 1346, 'hallo sie', 'beste', '1636220175.png', 0, 1, '2021-11-06 12:06:15', '2021-11-06 12:06:15'),
(21, 1347, 'h', 'H', '1636220292.png', 0, 1, '2021-11-06 12:08:12', '2021-11-06 12:08:12');

-- --------------------------------------------------------

--
-- Table structure for table `post_ads`
--

CREATE TABLE `post_ads` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `title` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `post_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1=> love ad, 2=> connect ad',
  `image` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `location` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `latitude` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `longitude` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_post_anonymously` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1=> show, 0=> profile hidden',
  `radius` decimal(8,2) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1=> active, 0=> not active',
  `approval` enum('0','1') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `post_likes`
--

CREATE TABLE `post_likes` (
  `id` bigint(20) NOT NULL,
  `post_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1=like, 0=unlike',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `post_likes`
--

INSERT INTO `post_likes` (`id`, `post_id`, `user_id`, `status`, `created_at`, `updated_at`) VALUES
(19, 20, 1347, 1, '2021-11-06 17:37:09', '2021-11-06 17:37:09'),
(20, 21, 1346, 1, '2021-11-06 17:51:54', '2021-11-06 17:51:54'),
(21, 20, 1346, 1, '2021-11-06 17:54:26', '2021-11-06 17:54:26');

-- --------------------------------------------------------

--
-- Table structure for table `qb_chat_dialog_ids`
--

CREATE TABLE `qb_chat_dialog_ids` (
  `id` int(11) NOT NULL,
  `match_id` int(11) DEFAULT NULL,
  `qb_dialog_id` varchar(255) DEFAULT NULL,
  `chat_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1=on, 0=off',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `qb_chat_dialog_ids`
--

INSERT INTO `qb_chat_dialog_ids` (`id`, `match_id`, `qb_dialog_id`, `chat_status`, `created_at`, `updated_at`) VALUES
(1, 978, '5df9ae0ba28f9a31fab995e3', 1, '2019-12-18 04:41:47', '2019-12-18 04:41:47'),
(4, 980, '5df9badfa28f9a27cdb995ee', 1, '2019-12-18 05:38:09', '2019-12-18 05:38:09'),
(10, 990, '5e04871da28f9a51949fe3ba', 1, '2019-12-26 10:10:37', '2019-12-26 10:10:37'),
(11, 989, '5e048dc9a0eb4768bc74cf1e', 1, '2019-12-26 10:39:05', '2019-12-26 10:39:05'),
(16, 987, '5e0ef0f2a0eb47633e74cf1f', 1, '2020-01-03 07:44:56', '2020-01-03 07:44:56'),
(22, 999, '5e142f39a28f9a7d364df2b4', 1, '2020-01-07 07:12:00', '2020-01-07 07:12:00'),
(23, 1002, '5e147feea0eb4742dee6eda3', 1, '2020-01-07 12:56:19', '2020-01-07 12:56:19'),
(24, 1003, '5e148f9ea28f9a04724df2b5', 1, '2020-01-07 14:03:16', '2020-01-07 14:03:16'),
(25, 1000, '5e155ebea0eb472527e6eda4', 1, '2020-01-08 04:47:00', '2020-01-08 04:47:00'),
(26, 1005, '5e156318a0eb47506be6eda3', 1, '2020-01-08 05:05:32', '2020-01-08 05:05:32'),
(27, 1006, '5e156c84a0eb475a4ce6eda3', 1, '2020-01-08 05:45:40', '2020-01-08 05:45:40'),
(29, 1008, '5e1c05f6a28f9a2ae400c797', 1, '2020-01-13 05:53:59', '2020-01-13 05:53:59'),
(30, 1009, '5e1c1050a28f9a660b00c797', 1, '2020-01-13 06:40:21', '2020-01-13 06:40:21'),
(31, 993, '5e1c1479a28f9a637100c797', 1, '2020-01-13 06:55:53', '2020-01-13 06:55:53'),
(32, 991, '5e1c1480a28f9a637100c798', 1, '2020-01-13 06:56:00', '2020-01-13 06:56:00'),
(35, 988, '5e1c1486a28f9a63e600c797', 1, '2020-01-13 07:02:09', '2020-01-13 07:02:09'),
(39, 1011, '5e1effeda28f9a484300c797', 1, '2020-01-15 12:05:19', '2020-01-15 12:05:19'),
(54, 1007, '5e217897a0eb473742e01762', 1, '2020-01-17 09:04:38', '2020-01-17 09:04:38'),
(57, 985, '5e219974a28f9a54579f10bb', 1, '2020-01-17 11:24:37', '2020-01-17 11:24:37'),
(58, 1012, '5e21a340a0eb47332ae01762', 1, '2020-01-17 12:06:24', '2020-01-17 12:06:24'),
(59, 1030, '5e21a924a28f9a4f5b9f10bb', 1, '2020-01-17 12:31:32', '2020-01-17 12:31:32'),
(63, 1036, '5e32b2e1a28f9a6f0f3b07ab', 1, '2020-01-30 10:41:37', '2020-01-30 10:41:37'),
(64, 1037, '5e32b4fda0eb474b9ffbc317', 1, '2020-01-30 10:50:38', '2020-01-30 10:50:38'),
(65, 1038, '5e33b0b7a0eb4708cffbc316', 1, '2020-01-31 04:44:45', '2020-01-31 04:44:45'),
(66, 1044, '5e33b717a0eb4729fffbc314', 1, '2020-01-31 05:11:57', '2020-01-31 05:11:57'),
(67, 1033, '5e3ba2a0a0eb47027c1f2065', 1, '2020-02-06 05:22:45', '2020-02-06 05:22:45'),
(68, 1039, '5e3ba5aaa28f9a66d6faae6c', 1, '2020-02-06 05:35:44', '2020-02-06 05:35:44'),
(69, 1045, '5e3bab5aa0eb4709511f2065', 1, '2020-02-06 06:00:19', '2020-02-06 06:00:19'),
(70, 1032, '5e3babbaa0eb470e741f2065', 1, '2020-02-06 06:01:34', '2020-02-06 06:01:34'),
(71, 1043, '5e3bb28da0eb4707f31f2065', 1, '2020-02-06 06:30:37', '2020-02-06 06:30:37'),
(77, 1014, '5e3bde47a0eb470cf21f2065', 1, '2020-02-06 09:37:15', '2020-02-06 09:37:15'),
(79, 1041, '5e3bde20a0eb470a5a1f2065', 1, '2020-02-06 11:48:07', '2020-02-06 11:48:07'),
(81, 998, '5e3c044fa0eb4777d11f2067', 1, '2020-02-06 12:19:27', '2020-02-06 12:19:27'),
(84, 1027, '5e3cedc7a0eb470ed20f1766', 1, '2020-02-07 04:55:53', '2020-02-07 04:55:53'),
(85, 997, '5e3cee01a28f9a17d3f4b168', 1, '2020-02-07 04:56:37', '2020-02-07 04:56:37'),
(86, 1026, '5e3cee95a28f9a3beef4b167', 1, '2020-02-07 04:59:25', '2020-02-07 04:59:25'),
(87, 1052, '5e3d01e2a0eb4702fa0f1767', 1, '2020-02-07 06:21:28', '2020-02-07 06:21:28'),
(88, 1054, '5e3d0571a28f9a547ef4b167', 1, '2020-02-07 06:37:01', '2020-02-07 06:37:01'),
(89, 1055, '5e3d0639a28f9a2d59f4b167', 1, '2020-02-07 06:41:11', '2020-02-07 06:41:11'),
(90, 1057, '5e3d0865a0eb476d030f1766', 1, '2020-02-07 06:49:32', '2020-02-07 06:49:32'),
(91, 1058, '5e3d0b4fa0eb47552e0f1767', 1, '2020-02-07 07:02:05', '2020-02-07 07:02:05'),
(92, 1060, '5e3d2df3a0eb47175a0f1766', 1, '2020-02-07 09:30:15', '2020-02-07 09:30:15'),
(93, 1061, '5e3d301ba0eb4701430f1767', 1, '2020-02-07 09:39:05', '2020-02-07 09:39:05'),
(94, 1062, '5e3d3bd8a0eb470cf40f1766', 1, '2020-02-07 10:28:55', '2020-02-07 10:28:55'),
(95, 1063, '5e4b757aa0eb471a3c8e673d', 1, '2020-02-18 05:26:19', '2020-02-18 05:26:19'),
(96, 1065, '5f8e9245a0eb4709c0f39d69', 1, '2020-10-20 07:36:02', '2020-10-20 07:36:02'),
(97, 1066, '5f8e924ba28f9ae672b1f611', 1, '2020-10-20 08:26:56', '2020-10-20 08:26:56'),
(99, 1081, '6066c635a0eb472523f5e966', 1, '2021-04-02 07:42:31', '2021-04-02 07:42:31'),
(100, 1074, '606abb4ea0eb47257af5f466', 1, '2021-04-05 07:25:08', '2021-04-05 07:25:08'),
(101, 1085, '606c515fa28f9a650c9bda23', 1, '2021-04-06 12:17:36', '2021-04-06 12:17:36'),
(103, 1088, '606da949a28f9a64f19be0f2', 1, '2021-04-07 12:47:20', '2021-04-07 12:47:20'),
(104, 1094, '6073d801a28f9a7cdaefbc6a', 1, '2021-04-12 05:17:59', '2021-04-12 05:17:59'),
(105, 1097, '6074bb19a0eb472c25961c5f', 1, '2021-04-12 22:04:18', '2021-04-12 22:04:18'),
(106, 1098, '607540a1a28f9a7cdaefc099', 1, '2021-04-13 06:57:44', '2021-04-13 06:57:44'),
(107, 1100, '60759736a0eb472be7962061', 1, '2021-04-13 13:07:30', '2021-04-13 13:07:30'),
(108, 1101, '607669b8a0eb472c0b962213', 1, '2021-04-14 04:04:14', '2021-04-14 04:04:14'),
(109, 1104, '60767c16a0eb472c0b962241', 1, '2021-04-14 05:27:16', '2021-04-14 05:27:16'),
(110, 0, '6076892aa28f9a7d15efc5be', 1, '2021-04-14 06:18:49', '2021-04-14 06:18:49'),
(111, 1106, '60768f97a28f9a7cdaefc414', 1, '2021-04-14 06:45:57', '2021-04-14 06:45:57'),
(112, 1108, '60769baaa0eb472c0b962284', 1, '2021-04-14 07:40:25', '2021-04-14 07:40:25'),
(113, 1112, '6076b832a28f9a7cc6efc59f', 1, '2021-04-14 09:42:01', '2021-04-14 09:42:01'),
(114, 1113, '6076bc49a28f9a7d06efc450', 1, '2021-04-14 09:56:48', '2021-04-14 09:56:48'),
(115, 1115, '6076bf6fa0eb472c0b9622df', 1, '2021-04-14 10:09:59', '2021-04-14 10:09:59'),
(116, 1118, '6076ce12a0eb472c32962110', 1, '2021-04-14 11:12:33', '2021-04-14 11:12:33'),
(117, 1119, '6076d321a0eb472c41961f6b', 1, '2021-04-14 11:34:49', '2021-04-14 11:34:49'),
(118, 1120, '6076dd16a28f9a7cdaefc4eb', 1, '2021-04-14 12:17:46', '2021-04-14 12:17:46'),
(121, 1134, '616db98df3a4530066a6cfce', 1, '2021-10-18 18:14:41', '2021-10-18 18:14:41'),
(122, 1135, '61768cd9b8f37e00473d33cb', 1, '2021-10-25 10:54:33', '2021-10-25 10:54:33'),
(123, 1136, '6178402ab8f37e00473d377d', 1, '2021-10-26 17:52:06', '2021-10-26 17:52:06'),
(124, 1138, '617841145ec5b00068f785cf', 1, '2021-10-26 17:55:36', '2021-10-26 17:55:36'),
(125, 1139, '61793804bbd7c500300155f8', 1, '2021-10-27 11:29:27', '2021-10-27 11:29:27'),
(126, 1140, '617938ab1558ea0080ce2c89', 1, '2021-10-27 11:33:38', '2021-10-27 11:33:38'),
(127, 1142, '61793ac41558ea0059ce2c71', 1, '2021-10-27 13:02:33', '2021-10-27 13:02:33'),
(128, 1143, '617a95621558ea002fce2f62', 1, '2021-10-28 12:23:25', '2021-10-28 12:23:25'),
(129, 1144, '617af8571558ea0049ce3188', 1, '2021-10-28 19:22:07', '2021-10-28 19:22:07'),
(130, 1145, '617af8841558ea0059ce3109', 1, '2021-10-28 19:22:52', '2021-10-28 19:22:52'),
(131, 1149, '617afa3cbbd7c50030015a6e', 1, '2021-10-28 19:30:08', '2021-10-28 19:30:08'),
(132, 1150, '617fd29e1558ea0049ce3deb', 1, '2021-11-01 11:42:46', '2021-11-01 11:42:46'),
(133, 1151, '6186b9d317897200536d8eb1', 1, '2021-11-06 17:22:56', '2021-11-06 17:22:56');

-- --------------------------------------------------------

--
-- Table structure for table `questions`
--

CREATE TABLE `questions` (
  `id` int(11) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1=active,0=inactive',
  `max_scores` int(11) DEFAULT NULL,
  `max_answers` int(11) DEFAULT NULL,
  `score_multiple` float NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `questions`
--

INSERT INTO `questions` (`id`, `status`, `max_scores`, `max_answers`, `score_multiple`, `created_at`, `updated_at`) VALUES
(1, 1, NULL, 3, 1, '2019-04-30 15:31:17', '2019-06-06 16:00:53'),
(2, 1, NULL, 1, 1, '2019-04-30 15:31:17', '2019-05-06 16:12:27'),
(3, 1, 20, 1, 1, '2019-04-30 15:31:17', '2019-04-30 16:17:13'),
(4, 1, 20, 1, 1, '2019-04-30 15:31:17', '2019-04-30 16:17:43'),
(5, 1, 80, 2, 2, '2019-04-30 15:31:17', '2019-04-30 16:19:26'),
(6, 1, 40, 2, 1, '2019-04-30 15:31:17', '2019-04-30 16:19:43'),
(7, 1, 40, 3, 1, '2019-04-30 15:31:17', '2019-04-30 16:19:59'),
(8, 0, 30, 2, 1, '2019-04-30 15:31:17', '2019-09-24 15:03:15'),
(9, 0, 9, 3, 0.15, '2019-04-30 15:31:17', '2019-09-24 15:03:23'),
(10, 0, 20, 1, 1, '2019-04-30 15:31:17', '2019-09-24 15:03:27'),
(11, 0, 20, 1, 1, '2019-04-30 15:31:17', '2019-09-24 15:03:30'),
(12, 0, 20, 1, 1, '2019-04-30 15:31:17', '2019-09-24 15:03:35'),
(13, 1, 60, 3, 1, '2019-04-30 15:31:17', '2019-04-30 16:23:38'),
(14, 0, 20, 1, 1, '2019-04-30 15:31:17', '2019-09-24 15:03:51'),
(15, 1, 60, 3, 1, '2019-04-30 15:31:17', '2019-04-30 16:25:36'),
(16, 1, 20, 1, 1, '2019-04-30 15:31:17', '2019-04-30 16:25:46'),
(17, 0, 20, 1, 1, '2019-04-30 15:31:17', '2019-09-24 15:03:55'),
(18, 0, 20, 1, 1, '2019-04-30 15:31:17', '2019-09-24 15:03:59'),
(19, 0, NULL, NULL, 1, '2019-04-30 15:31:17', '2019-04-30 15:59:31');

-- --------------------------------------------------------

--
-- Table structure for table `question_descs`
--

CREATE TABLE `question_descs` (
  `question_id` int(11) NOT NULL,
  `language_id` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1=English,2=German',
  `question` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1=active,0=inactive',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `question_descs`
--

INSERT INTO `question_descs` (`question_id`, `language_id`, `question`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 'What are you looking for on Love Push?', 1, '2019-04-30 15:30:10', '2019-06-03 19:43:57'),
(1, 2, 'Wonach suchst du bei Love Push?', 1, '2019-04-30 15:30:10', '2019-06-03 19:44:07'),
(2, 1, 'What are your ambitions?', 1, '2019-04-30 15:33:28', '2019-06-03 19:45:27'),
(2, 2, 'Was sind Ihre Ziele?', 1, '2019-04-30 15:33:28', '2019-06-03 19:45:37'),
(3, 1, 'What is your favourite spot?', 1, '2019-04-30 15:33:28', '2019-09-24 15:13:08'),
(3, 2, 'Was ist dein Lieblingsplatz?', 1, '2019-04-30 15:33:28', '2019-06-03 19:49:08'),
(4, 1, 'Your fashion styles?', 1, '2019-04-30 15:33:28', '2019-10-30 06:10:46'),
(4, 2, 'Deine Modestile?', 1, '2019-04-30 15:33:28', '2019-09-24 15:14:16'),
(5, 1, 'What are your reasons for meeting new people?', 1, '2019-04-30 15:33:28', '2019-09-24 15:14:39'),
(5, 2, 'Was sind Ihre Gründe, neue Leute kennenzulernen?', 1, '2019-04-30 15:33:28', '2019-09-24 15:14:52'),
(6, 1, 'How would you describe yourself?', 1, '2019-04-30 15:33:28', '2019-10-21 12:09:58'),
(6, 2, 'Wie würdest du dich selbst beschreiben?', 1, '2019-04-30 15:33:28', '2019-04-30 15:35:45'),
(7, 1, 'How do you spend your weekends?', 1, '2019-04-30 15:33:28', '2019-04-30 15:35:36'),
(7, 2, 'Wie verbringst du deine Wochenenden?', 1, '2019-04-30 15:33:28', '2019-04-30 15:35:45'),
(8, 1, 'How do you prefer to get your food?', 0, '2019-04-30 15:33:28', '2019-09-24 14:59:36'),
(8, 2, 'kochen, bestellen oder essen gehen?', 0, '2019-04-30 15:33:28', '2019-09-24 14:59:40'),
(9, 1, 'Which national cuisine do you prefer?', 0, '2019-04-30 15:33:28', '2019-09-24 14:59:46'),
(9, 2, 'Welche Mahlzeiten bevorzugen Sie?', 0, '2019-04-30 15:33:28', '2019-09-24 14:59:50'),
(10, 1, 'What morning guy are you?', 0, '2019-04-30 15:33:28', '2019-09-24 14:59:54'),
(10, 2, 'Wenn du schlafen kannst, wann stehst du auf?', 0, '2019-04-30 15:33:28', '2019-09-24 14:59:57'),
(11, 1, 'Which animal do you prefer?', 0, '2019-04-30 15:33:28', '2019-09-24 15:00:02'),
(11, 2, 'Welches Tier bevorzugst du?', 0, '2019-04-30 15:33:28', '2019-09-24 15:00:09'),
(12, 1, 'What is your level of education?', 0, '2019-04-30 15:33:28', '2019-09-24 15:00:13'),
(12, 2, 'Was ist Ihr Bildungsstand?', 0, '2019-04-30 15:33:28', '2019-09-24 15:00:18'),
(13, 1, 'What are your favourite free time activities?', 1, '2019-04-30 15:33:28', '2019-06-04 16:47:39'),
(13, 2, 'Was sind Ihre Lieblingsfreizeitbeschäftigungen?', 1, '2019-04-30 15:33:28', '2019-06-04 16:47:58'),
(14, 1, 'If you win a trip from Love Push, where would it be?', 0, '2019-04-30 15:33:28', '2019-09-24 15:00:28'),
(14, 2, 'Wenn Sie eine Reise von Love Push gewinnen, wo würde es sein?', 0, '2019-04-30 15:33:28', '2019-09-24 15:00:32'),
(15, 1, 'What is most important referring to your dream partner?', 1, '2019-04-30 15:33:28', '2019-09-24 15:16:43'),
(15, 2, 'Was ist für Ihren Traumpartner am wichtigsten?', 1, '2019-04-30 15:33:28', '2019-09-24 15:16:53'),
(16, 1, 'How important is sex for you in a relationship?', 1, '2019-04-30 15:33:28', '2019-10-30 06:13:46'),
(16, 2, 'Wie wichtig ist Sex in einer Beziehung für dich?', 1, '2019-04-30 15:33:28', '2019-04-30 15:55:37'),
(17, 1, 'Are you planning to have kids with your partner?', 0, '2019-04-30 15:33:28', '2019-09-24 15:01:29'),
(17, 2, 'Planen Sie Kinder mit Ihrem Partner zu haben?', 0, '2019-04-30 15:33:28', '2019-09-24 15:01:33'),
(18, 1, 'Can you imagine getting married?', 0, '2019-04-30 15:33:28', '2019-09-24 15:01:21'),
(18, 2, 'Kannst du dir vorstellen zu heiraten?', 0, '2019-04-30 15:33:28', '2019-09-24 15:01:25');

-- --------------------------------------------------------

--
-- Table structure for table `ques_options`
--

CREATE TABLE `ques_options` (
  `id` int(11) NOT NULL,
  `question_id` int(11) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1=active,0=inactive',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ques_options`
--

INSERT INTO `ques_options` (`id`, `question_id`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 1, '2019-04-30 17:02:46', '2019-04-30 17:02:46'),
(2, 1, 1, '2019-04-30 17:02:46', '2019-04-30 17:04:16'),
(3, 1, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:18'),
(4, 2, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(5, 2, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(6, 2, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(7, 2, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(8, 2, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(9, 3, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(10, 3, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(11, 3, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(12, 3, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(13, 4, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(14, 4, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(15, 4, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(16, 4, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(17, 5, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(18, 5, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(19, 5, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(20, 5, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(21, 5, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(22, 6, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(23, 6, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(24, 6, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(25, 6, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(26, 6, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(27, 6, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(28, 6, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(29, 6, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(30, 7, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(31, 7, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(32, 7, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(33, 7, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(34, 7, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(35, 7, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(36, 7, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(37, 7, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(38, 8, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(39, 8, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(40, 8, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(41, 8, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(42, 8, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(43, 8, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(44, 9, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(45, 9, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(46, 9, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(47, 9, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(48, 9, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(49, 9, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(50, 9, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(51, 9, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(52, 10, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(53, 10, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(54, 10, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(55, 11, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(56, 11, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(57, 11, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(58, 12, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(59, 12, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(60, 12, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(61, 12, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(62, 12, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(63, 12, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(64, 12, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(65, 13, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(66, 13, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(67, 13, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(68, 13, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(69, 13, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(70, 13, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(71, 13, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(72, 13, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(73, 13, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(74, 14, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(75, 14, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(76, 14, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(77, 14, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(78, 14, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(79, 15, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(80, 15, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(81, 15, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(82, 15, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(83, 15, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(84, 15, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(85, 15, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(86, 16, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(87, 16, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(88, 16, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(89, 16, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(90, 17, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(91, 17, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(92, 17, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(93, 18, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(94, 18, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51'),
(95, 18, 1, '2019-04-30 17:02:51', '2019-04-30 17:04:51');

-- --------------------------------------------------------

--
-- Table structure for table `ques_option_descs`
--

CREATE TABLE `ques_option_descs` (
  `ques_option_id` int(11) NOT NULL,
  `language_id` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1=English, 2=German',
  `option` text CHARACTER SET utf8,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1=active,0=inactive',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ques_option_descs`
--

INSERT INTO `ques_option_descs` (`ques_option_id`, `language_id`, `option`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 'Man', 1, '0000-00-00 00:00:00', '2019-04-30 17:07:47'),
(1, 2, 'Mann', 1, '0000-00-00 00:00:00', '2019-04-30 17:07:47'),
(2, 1, 'Woman', 1, '0000-00-00 00:00:00', '2019-07-05 11:13:26'),
(2, 2, 'Frau', 1, '0000-00-00 00:00:00', '2019-04-30 17:07:47'),
(3, 1, 'Other', 1, '0000-00-00 00:00:00', '2019-10-30 06:14:42'),
(3, 2, 'Andere', 1, '0000-00-00 00:00:00', '2019-10-23 18:26:57'),
(4, 1, 'Love', 1, '0000-00-00 00:00:00', '2019-06-03 19:47:57'),
(4, 2, 'Love', 1, '0000-00-00 00:00:00', '2019-06-03 19:48:03'),
(5, 1, 'Flirts', 1, '0000-00-00 00:00:00', '2019-06-03 19:48:07'),
(5, 2, 'Flirts', 1, '0000-00-00 00:00:00', '2019-06-03 19:48:10'),
(6, 1, 'Friends', 1, '0000-00-00 00:00:00', '2019-06-03 19:48:15'),
(6, 2, 'Friends', 1, '0000-00-00 00:00:00', '2019-06-03 19:48:18'),
(7, 1, 'Travel buddy\'s', 1, '0000-00-00 00:00:00', '2019-10-30 06:25:54'),
(7, 2, 'Reisepartner', 1, '0000-00-00 00:00:00', '2019-10-30 06:30:12'),
(8, 1, 'Open minded', 1, '0000-00-00 00:00:00', '2019-06-03 19:48:37'),
(8, 2, 'Open minded', 1, '0000-00-00 00:00:00', '2019-06-03 19:48:39'),
(9, 1, 'Outside I love to move and be in nature', 1, '0000-00-00 00:00:00', '2019-10-23 18:35:50'),
(9, 2, 'Draußen mag ich mich bewegen und in der Natur sein', 1, '0000-00-00 00:00:00', '2019-10-23 18:36:05'),
(10, 1, 'My home is my temple', 1, '0000-00-00 00:00:00', '2019-04-30 17:07:47'),
(10, 2, 'Mein Zuhause ist mein Tempel', 1, '0000-00-00 00:00:00', '2019-04-30 17:07:47'),
(11, 1, 'Everywhere surrounded by nice people', 1, '0000-00-00 00:00:00', '2019-06-03 19:53:48'),
(11, 2, 'Orte, an denen ich neue interessante Leute kennenlernen kann', 1, '0000-00-00 00:00:00', '2019-04-30 17:07:47'),
(12, 1, 'Places for inspiration', 1, '0000-00-00 00:00:00', '2019-06-03 19:53:58'),
(12, 2, 'Ein Ort, an dem ich nachdenken und neue Ideen finden kann', 1, '0000-00-00 00:00:00', '2019-04-30 17:07:47'),
(13, 1, 'Casual', 1, '0000-00-00 00:00:00', '2019-06-03 19:54:37'),
(13, 2, 'Casual', 1, '0000-00-00 00:00:00', '2019-06-03 19:54:45'),
(14, 1, 'Glamorous', 1, '0000-00-00 00:00:00', '2019-10-23 18:36:58'),
(14, 2, 'Glamorous', 1, '0000-00-00 00:00:00', '2019-10-23 18:37:16'),
(15, 1, 'Comfortable', 1, '0000-00-00 00:00:00', '2019-06-03 19:55:09'),
(15, 2, 'Comfortable', 1, '0000-00-00 00:00:00', '2019-06-03 19:55:12'),
(16, 1, 'Different', 1, '0000-00-00 00:00:00', '2019-06-03 19:55:18'),
(16, 2, 'Different', 1, '0000-00-00 00:00:00', '2019-06-03 19:55:21'),
(17, 1, 'Love and security', 1, '0000-00-00 00:00:00', '2019-04-30 17:40:17'),
(17, 2, 'Liebe und Sicherheit', 1, '0000-00-00 00:00:00', '2019-04-30 17:40:25'),
(18, 1, 'Money and status', 1, '0000-00-00 00:00:00', '2019-04-30 17:40:39'),
(18, 2, 'Geld und Status', 1, '0000-00-00 00:00:00', '2019-04-30 17:40:46'),
(19, 1, 'Free time activities and hobby’s', 1, '0000-00-00 00:00:00', '2019-10-30 06:31:20'),
(19, 2, 'Freizeitaktivitäten und Hobby', 1, '0000-00-00 00:00:00', '2019-10-23 18:38:32'),
(20, 1, 'Creating good memories', 1, '0000-00-00 00:00:00', '2019-09-24 15:22:55'),
(20, 2, 'Gute Erinnerungen schaffen', 1, '0000-00-00 00:00:00', '2019-09-24 15:23:06'),
(21, 1, 'Becoming old together', 1, '0000-00-00 00:00:00', '2019-09-24 15:23:29'),
(21, 2, 'Gemeinsam alt werden', 1, '0000-00-00 00:00:00', '2019-09-24 15:23:41'),
(22, 1, 'Shy', 1, '0000-00-00 00:00:00', '2019-09-24 15:24:24'),
(22, 2, 'Schüchtern', 1, '0000-00-00 00:00:00', '2019-09-24 15:24:32'),
(23, 1, 'Depending on my mood', 1, '0000-00-00 00:00:00', '2019-09-24 15:24:41'),
(23, 2, 'Abhängig von meiner Stimmung', 1, '0000-00-00 00:00:00', '2019-09-24 15:25:05'),
(24, 1, 'Open minded', 1, '0000-00-00 00:00:00', '2019-09-24 15:25:15'),
(24, 2, 'Offen', 1, '0000-00-00 00:00:00', '2019-09-24 15:25:22'),
(25, 1, 'Humorous', 1, '0000-00-00 00:00:00', '2019-09-24 15:25:31'),
(25, 2, 'Humorvoll', 1, '0000-00-00 00:00:00', '2019-09-24 15:25:39'),
(26, 1, 'Sensitive', 1, '0000-00-00 00:00:00', '2019-10-23 18:40:10'),
(26, 2, 'Empfindlich', 1, '0000-00-00 00:00:00', '2019-10-23 18:41:23'),
(27, 1, 'Emotional', 1, '0000-00-00 00:00:00', '2019-10-23 18:40:05'),
(27, 2, 'Emotional', 1, '0000-00-00 00:00:00', '2019-10-23 18:41:35'),
(28, 1, 'Optimistic', 1, '0000-00-00 00:00:00', '2019-10-23 18:40:35'),
(28, 2, 'Optimistisch', 1, '0000-00-00 00:00:00', '2019-10-23 18:41:46'),
(29, 1, 'Pessimistic', 1, '0000-00-00 00:00:00', '2019-10-23 18:41:02'),
(29, 2, 'Pessimistisch', 1, '0000-00-00 00:00:00', '2019-10-23 18:41:10'),
(30, 1, 'Netflix & TV', 1, '0000-00-00 00:00:00', '2019-04-30 17:56:30'),
(30, 2, 'Netflix & TV', 1, '0000-00-00 00:00:00', '2019-04-30 17:56:39'),
(31, 1, 'Bar hopping with friends', 1, '0000-00-00 00:00:00', '2019-04-30 17:56:47'),
(31, 2, 'Barhopping mit Freunden', 1, '0000-00-00 00:00:00', '2019-04-30 17:56:55'),
(32, 1, 'Club or other Party locations', 1, '0000-00-00 00:00:00', '2019-04-30 17:57:05'),
(32, 2, 'Club- oder andere Partyorte', 1, '0000-00-00 00:00:00', '2019-04-30 17:57:13'),
(33, 1, 'Short trip', 1, '0000-00-00 00:00:00', '2019-10-23 18:42:27'),
(33, 2, 'Kurzausflug', 1, '0000-00-00 00:00:00', '2019-04-30 17:57:31'),
(34, 1, 'Wellness', 1, '0000-00-00 00:00:00', '2019-10-23 18:42:43'),
(34, 2, 'Wellness', 1, '0000-00-00 00:00:00', '2019-04-30 17:57:48'),
(35, 1, 'Go out for dinner', 1, '0000-00-00 00:00:00', '2019-10-23 18:42:56'),
(35, 2, 'Essen gehen', 1, '0000-00-00 00:00:00', '2019-04-30 17:58:06'),
(36, 1, 'Sports', 1, '0000-00-00 00:00:00', '2019-09-24 15:29:51'),
(36, 2, 'Sport', 1, '0000-00-00 00:00:00', '2019-09-24 15:29:59'),
(37, 1, 'With my family', 1, '0000-00-00 00:00:00', '2019-10-23 18:43:14'),
(37, 2, 'Mit meiner Familie', 1, '0000-00-00 00:00:00', '2019-10-23 18:43:24'),
(38, 1, 'I like to cook at home', 1, '0000-00-00 00:00:00', '2019-04-30 18:19:53'),
(38, 2, 'Ich koche gerne zu Hause', 1, '0000-00-00 00:00:00', '2019-04-30 18:20:11'),
(39, 1, 'I like to get cooks', 1, '0000-00-00 00:00:00', '2019-06-03 19:56:26'),
(39, 2, 'Ich mag es, Köche zu bekommen', 1, '0000-00-00 00:00:00', '2019-04-30 18:20:25'),
(40, 1, 'ready meals at home', 1, '0000-00-00 00:00:00', '2019-04-30 18:20:46'),
(40, 2, 'Fertiggerichte zu Hause', 1, '0000-00-00 00:00:00', '2019-04-30 18:20:54'),
(41, 1, 'delivery service', 1, '0000-00-00 00:00:00', '2019-04-30 18:21:03'),
(41, 2, 'Lieferservice', 1, '0000-00-00 00:00:00', '2019-04-30 18:21:13'),
(42, 1, 'Fast food', 1, '0000-00-00 00:00:00', '2019-04-30 18:21:22'),
(42, 2, 'Fast Food', 1, '0000-00-00 00:00:00', '2019-04-30 18:21:31'),
(43, 1, 'restaurans', 1, '0000-00-00 00:00:00', '2019-04-30 18:21:57'),
(43, 2, 'Restaurants', 1, '0000-00-00 00:00:00', '2019-04-30 18:22:03'),
(44, 1, 'German', 1, '0000-00-00 00:00:00', '2019-04-30 18:36:13'),
(44, 2, 'German', 1, '0000-00-00 00:00:00', '2019-04-30 18:36:15'),
(45, 1, 'Italian', 1, '0000-00-00 00:00:00', '2019-04-30 18:36:06'),
(45, 2, 'Italian', 1, '0000-00-00 00:00:00', '2019-04-30 18:36:04'),
(46, 1, 'Asian', 1, '0000-00-00 00:00:00', '2019-04-30 18:35:57'),
(46, 2, 'Asian', 1, '0000-00-00 00:00:00', '2019-04-30 18:35:54'),
(47, 1, 'African', 1, '0000-00-00 00:00:00', '2019-04-30 18:35:48'),
(47, 2, 'African', 1, '0000-00-00 00:00:00', '2019-04-30 18:35:46'),
(48, 1, 'american', 1, '0000-00-00 00:00:00', '2019-04-30 18:35:41'),
(48, 2, 'american', 1, '0000-00-00 00:00:00', '2019-04-30 18:35:39'),
(49, 1, 'greek', 1, '0000-00-00 00:00:00', '2019-04-30 18:35:31'),
(49, 2, 'greek', 1, '0000-00-00 00:00:00', '2019-04-30 18:35:28'),
(50, 1, 'oriental', 1, '0000-00-00 00:00:00', '2019-04-30 18:35:19'),
(50, 2, 'oriental', 1, '0000-00-00 00:00:00', '2019-04-30 18:35:23'),
(51, 1, 'international', 1, '0000-00-00 00:00:00', '2019-04-30 18:34:56'),
(51, 2, 'International', 1, '0000-00-00 00:00:00', '2019-04-30 18:35:09'),
(52, 1, 'The early bird', 1, '0000-00-00 00:00:00', '2019-06-03 19:57:57'),
(52, 2, 'Der Wecker klingelt um 6 Uhr, damit ich den Tag optimal nutzen kann', 1, '0000-00-00 00:00:00', '2019-04-30 18:39:28'),
(53, 1, 'The easy way', 1, '0000-00-00 00:00:00', '2019-06-03 19:58:05'),
(53, 2, 'Ich stehe auf, wenn ich aufwache', 1, '0000-00-00 00:00:00', '2019-04-30 18:39:47'),
(54, 1, 'The long sleeper', 1, '0000-00-00 00:00:00', '2019-06-03 19:58:10'),
(54, 2, 'Keine Möglichkeit, mich vor 12 Uhr aus dem Bett zu bekommen', 1, '0000-00-00 00:00:00', '2019-04-30 18:40:05'),
(55, 1, 'dog', 1, '0000-00-00 00:00:00', '2019-04-30 18:42:32'),
(55, 2, 'Hund', 1, '0000-00-00 00:00:00', '2019-04-30 18:42:41'),
(56, 1, 'cat', 1, '0000-00-00 00:00:00', '2019-04-30 18:42:47'),
(56, 2, 'Katze', 1, '0000-00-00 00:00:00', '2019-04-30 18:42:55'),
(57, 1, 'I don\'t like animals at all', 1, '0000-00-00 00:00:00', '2019-04-30 18:43:25'),
(57, 2, 'Ich mag keine Tiere', 1, '0000-00-00 00:00:00', '2019-04-30 18:43:15'),
(58, 1, 'no degree', 1, '0000-00-00 00:00:00', '2019-05-20 13:20:55'),
(58, 2, 'kein Abschluss', 1, '0000-00-00 00:00:00', '2019-05-20 13:21:40'),
(59, 1, 'certificate of secondary Education', 1, '0000-00-00 00:00:00', '2019-05-20 13:20:55'),
(59, 2, 'Abschluss des Sekundarbereichs', 1, '0000-00-00 00:00:00', '2019-05-20 13:21:40'),
(60, 1, 'General certificate of secondary Education', 1, '0000-00-00 00:00:00', '2019-05-20 13:20:55'),
(60, 2, 'Allgemeiner Sekundarschulabschluss', 1, '0000-00-00 00:00:00', '2019-05-20 13:21:40'),
(61, 1, 'Highschool / university-entrance-diploma', 1, '0000-00-00 00:00:00', '2019-05-20 13:20:55'),
(61, 2, 'Abitur / Hochschuldiplom', 1, '0000-00-00 00:00:00', '2019-05-20 13:21:40'),
(62, 1, 'professional education', 1, '0000-00-00 00:00:00', '2019-05-20 13:20:55'),
(62, 2, 'berufliche Bildung', 1, '0000-00-00 00:00:00', '2019-05-20 13:21:40'),
(63, 1, 'Bachelor / Masterdegree', 1, '0000-00-00 00:00:00', '2019-05-20 13:20:55'),
(63, 2, 'Bachelor / Master-Abschluss', 1, '0000-00-00 00:00:00', '2019-05-20 13:21:40'),
(64, 1, 'Doctor / Diploma', 1, '0000-00-00 00:00:00', '2019-05-20 13:20:55'),
(64, 2, 'Doktor / Diplom', 1, '0000-00-00 00:00:00', '2019-05-20 13:21:40'),
(65, 1, 'Sports and nature', 1, '0000-00-00 00:00:00', '2019-06-04 16:49:01'),
(65, 2, 'Sports and nature', 1, '0000-00-00 00:00:00', '2019-06-04 16:49:04'),
(66, 1, 'Relaxing on the couch', 1, '0000-00-00 00:00:00', '2019-06-04 16:49:12'),
(66, 2, 'Relaxing on the couch', 1, '0000-00-00 00:00:00', '2019-06-04 16:49:16'),
(67, 1, 'Meeting with friends', 1, '0000-00-00 00:00:00', '2019-06-04 16:49:22'),
(67, 2, 'Meeting with friends', 1, '0000-00-00 00:00:00', '2019-06-04 16:49:24'),
(68, 1, 'Shopping', 1, '0000-00-00 00:00:00', '2019-06-04 16:49:31'),
(68, 2, 'Shopping', 1, '0000-00-00 00:00:00', '2019-06-04 16:49:34'),
(69, 1, 'Reading books', 1, '0000-00-00 00:00:00', '2019-06-04 16:49:41'),
(69, 2, 'Reading books', 1, '0000-00-00 00:00:00', '2019-06-04 16:49:45'),
(70, 1, 'Creative things (art & music)', 1, '0000-00-00 00:00:00', '2019-06-04 16:49:53'),
(70, 2, 'Creative things (art & music)', 1, '0000-00-00 00:00:00', '2019-06-04 16:49:56'),
(71, 1, 'Spirituality & Self care', 1, '0000-00-00 00:00:00', '2019-10-30 06:36:12'),
(71, 2, 'Spiritualität & Selbstpflege', 1, '0000-00-00 00:00:00', '2019-10-30 06:36:29'),
(72, 1, 'PC & Video Games', 1, '0000-00-00 00:00:00', '2019-06-04 16:50:15'),
(72, 2, 'PC & Video Games', 1, '0000-00-00 00:00:00', '2019-06-04 16:50:18'),
(73, 1, 'Business', 1, '0000-00-00 00:00:00', '2019-10-30 06:36:56'),
(73, 2, 'Geschäft', 1, '0000-00-00 00:00:00', '2019-10-30 06:37:08'),
(74, 1, 'just at the ocean', 1, '0000-00-00 00:00:00', '2019-04-30 19:02:47'),
(74, 2, 'just at the ocean', 1, '0000-00-00 00:00:00', '2019-04-30 19:02:49'),
(75, 1, 'to the mountains for hiking or skiing', 1, '0000-00-00 00:00:00', '2019-04-30 19:02:56'),
(75, 2, 'to the mountains for hiking or skiing', 1, '0000-00-00 00:00:00', '2019-04-30 19:02:58'),
(76, 1, 'Sightseeing Tour and Museums', 1, '0000-00-00 00:00:00', '2019-05-24 18:48:19'),
(76, 2, 'Besichtigungstour und Museen', 1, '0000-00-00 00:00:00', '2019-05-24 18:48:31'),
(77, 1, 'Adventure holdiays', 1, '0000-00-00 00:00:00', '2019-04-30 19:03:28'),
(77, 2, 'Adventure holdiays', 1, '0000-00-00 00:00:00', '2019-04-30 19:03:30'),
(78, 1, 'I don’t need to go on vacation, I like to stay at home', 1, '0000-00-00 00:00:00', '2019-04-30 19:03:36'),
(78, 2, 'I don’t need to go on vacation, I like to stay at home', 1, '0000-00-00 00:00:00', '2019-04-30 19:03:39'),
(79, 1, 'Attractive appearance', 1, '0000-00-00 00:00:00', '2019-09-24 15:31:52'),
(79, 2, 'Attraktives Aussehen', 1, '0000-00-00 00:00:00', '2019-09-24 15:32:00'),
(80, 1, 'Common interests', 1, '0000-00-00 00:00:00', '2019-10-23 18:46:06'),
(80, 2, 'gemeinsame Interessen', 1, '0000-00-00 00:00:00', '2019-09-24 15:35:18'),
(81, 1, 'Intelligence', 1, '0000-00-00 00:00:00', '2019-10-23 18:45:57'),
(81, 2, 'Intelligenz', 1, '0000-00-00 00:00:00', '2019-09-24 15:35:34'),
(82, 1, 'Status and Money', 1, '0000-00-00 00:00:00', '2019-04-30 19:10:16'),
(82, 2, 'Status und Geld', 1, '0000-00-00 00:00:00', '2019-09-24 15:35:45'),
(83, 1, 'Empathy', 1, '0000-00-00 00:00:00', '2019-05-24 18:49:30'),
(83, 2, 'Empathie', 1, '0000-00-00 00:00:00', '2019-05-24 18:49:39'),
(84, 1, 'Loyalty', 1, '0000-00-00 00:00:00', '2019-04-30 19:09:51'),
(84, 2, 'Loyalität', 1, '0000-00-00 00:00:00', '2019-09-24 15:36:01'),
(85, 1, 'Honesty', 1, '0000-00-00 00:00:00', '2019-10-23 18:46:27'),
(85, 2, 'Ehrlichkeit', 1, '0000-00-00 00:00:00', '2019-09-24 15:33:10'),
(86, 1, 'As often as possible', 1, '0000-00-00 00:00:00', '2019-09-24 15:32:43'),
(86, 2, 'So oft wie möglich', 1, '0000-00-00 00:00:00', '2019-09-24 15:32:53'),
(87, 1, 'Sometime a week', 1, '0000-00-00 00:00:00', '2019-09-24 15:34:05'),
(87, 2, 'Irgendwann in der Woche', 1, '0000-00-00 00:00:00', '2019-09-24 15:34:10'),
(88, 1, 'In the right circumstances', 1, '0000-00-00 00:00:00', '2019-09-24 15:34:22'),
(88, 2, 'Unter den richtigen Umständen', 1, '0000-00-00 00:00:00', '2019-09-24 15:34:33'),
(89, 1, 'I don\'t care', 1, '0000-00-00 00:00:00', '2019-09-24 15:34:46'),
(89, 2, 'Ist mir egal', 1, '0000-00-00 00:00:00', '2019-09-24 15:34:53'),
(90, 1, 'Yes, when the time has come', 1, '0000-00-00 00:00:00', '2019-06-04 16:57:17'),
(90, 2, 'Yes, when the time has come', 1, '0000-00-00 00:00:00', '2019-06-04 16:57:27'),
(91, 1, 'no', 1, '0000-00-00 00:00:00', '2019-04-30 19:23:14'),
(91, 2, 'no', 1, '0000-00-00 00:00:00', '2019-04-30 19:23:16'),
(92, 1, 'What goes comes around', 1, '0000-00-00 00:00:00', '2019-06-04 16:57:37'),
(92, 2, 'What goes comes around', 1, '0000-00-00 00:00:00', '2019-06-04 16:57:39'),
(93, 1, 'Yes with the right partner', 1, '0000-00-00 00:00:00', '2019-06-04 16:57:52'),
(93, 2, 'Yes with the right partner', 1, '0000-00-00 00:00:00', '2019-06-04 16:57:55'),
(94, 1, 'no', 1, '0000-00-00 00:00:00', '2019-04-30 19:23:14'),
(94, 2, 'no', 1, '0000-00-00 00:00:00', '2019-04-30 19:23:16'),
(95, 1, 'Let\'s see', 1, '0000-00-00 00:00:00', '2019-06-04 16:58:01'),
(95, 2, 'Let\'s see', 1, '0000-00-00 00:00:00', '2019-06-04 16:58:04');

-- --------------------------------------------------------

--
-- Table structure for table `report_chat`
--

CREATE TABLE `report_chat` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `reported_user_id` int(11) NOT NULL,
  `message` longtext NOT NULL,
  `report_status` enum('0','1') NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `report_images`
--

CREATE TABLE `report_images` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `reported_user_id` int(11) DEFAULT NULL,
  `image_id` int(11) NOT NULL,
  `type` enum('profile_image','post','post_ad') NOT NULL,
  `status` enum('0','1') NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `static_pages`
--

CREATE TABLE `static_pages` (
  `id` int(11) NOT NULL,
  `page_name` varchar(255) NOT NULL,
  `page_heading` varchar(255) NOT NULL,
  `content` longtext NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `static_pages`
--

INSERT INTO `static_pages` (`id`, `page_name`, `page_heading`, `content`, `created_at`, `updated_at`) VALUES
(1, 'community_guidelines', 'Community Guidelines', '\n<p align="center"><span style="color: #000000;">Welcome to the LovePush Community! </span></p>\n\n<p align="center"><span style="color: #000000;">In case you\'re straightforward, kind and aware to other people, you\'ll generally be welcome here. </span></p>\n\n<p align="center"><span style="color: #000000;">On the off chance that you decide not to be, you may not last. We will probably enable clients to convey what needs be uninhibitedly as long as it doesn\'t affront others. </span></p>\n\n<p align="center"><span style="color: #000000;">Everybody is held to a similar standard on LovePush. </span></p>\n\n<p align="center"><span style="color: #000000;">We\'re requesting that you be chivalrous, think before you act, and comply with our locale rules both on and disconnected. </span></p>\n\n<p align="center"><span style="color: #000000;">You heard that right: your disconnected conduct can prompt end of your LovePush Account. </span></p>\n\n<p align="center"><span style="color: #000000;">The following is a rundown of our locale arrangements. In the event that you abuse any of these strategies, you may be restricted from LovePush. </span></p>\n\n<p align="center"><span style="color: #000000;">Truly, don\'t make us Swipe Left on you&mdash;in light of the fact that there will be no second chances once we do. </span></p>\n\n<p align="center"><span style="color: #000000;">We energize you to&nbsp;report&nbsp;any conduct that damages our approaches. </span></p>\n\n<p align="center"><span style="color: #000000;">Love Push isn\'t for: </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Nudity/Sexual Content</strong></span></span></strong></span></p>\n\n<p align="center"><span style="color: #000000;">We\'re not requesting that you brush your hair to the other side or even talk in full sentences; however please keep it tasteful and suitable for open utilization. No nakedness, no explicitly unequivocal substance, and don\'t narrative the majority of your sexual wants in your profile. Keep it clean. </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Harassment</strong></span></span></strong></span></p>\n\n<p align="center"><span style="color: #000000;">Try not to draw in, or urge others to connect with, in any focused on maltreatment or badgering against some other client. This incorporates sending any spontaneous sexual substance to your matches. Reports of stalking, dangers, tormenting, or terrorizing, are paid attention to very. </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Violence and Physical Harm</strong></span></span></strong></span></p>\n\n<p align="center"><span style="color: #000000;">We don\'t endure vicious, realistic, or violent substance on Love Push, or any activities or substance that supporter for or undermine brutality of any kind, including compromising or advancing fear based oppression. Physical strike, intimidation, and any demonstrations of brutality are carefully denied. </span></p>\n\n<p align="center"><span style="color: #000000;">Content that supporters for or extols suicide or self-hurt is additionally not permitted. In these circumstances, we may find a way to help the client, incorporating connecting with emergency assets. </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Hate Speech</strong></span></span></strong></span></p>\n\n<p align="center"><span style="color: #000000;">Any substance that advances, advocates for, or overlooks prejudice, dogmatism, scorn, or brutality against people or gatherings dependent on variables like (yet not restricted to) race, ethnicity, strict alliance, handicap, sex, age, national inception, sexual direction, or sex character isn\'t permitted. </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Private Information</strong></span></span></strong></span></p>\n\n<p align="center"><span style="color: #000000;">Don\'t openly communicate any private data, yours or anybody else\'s. This incorporates standardized savings numbers, travel papers, passwords, money related data or unlisted contact data, for example, telephone numbers, email addresses, home/place of business. </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Spam</strong></span></span></strong></span></p>\n\n<p align="center"><span style="color: #000000;">Try not to be phony. Be genuine. Try not to utilize Love Push to drive individuals to outside sites by means of a connection or something else. </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Promotion or Solicitation</strong></span></span></strong></span></p>\n\n<p align="center"><span style="color: #000000;">Requesting different clients is precluded on Love Push. It\'s fine to welcome your matches to something that you\'re doing, yet on the off chance that the motivation behind your profile is to publicize your occasion or business, non-benefit, political crusade, challenge, or to lead explore, we may erase your record. While we\'re energized that you\'re doing a parody appear one week from now, kindly don\'t utilize Love Push to advance it. </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Prostitution and Trafficking</strong></span></span></strong></span></p>\n\n<p align="center"><span style="color: #000000;">Advancing or pushing for business sexual administrations, human dealing or other non-consensual sexual acts is carefully disallowed and will bring about your record being prohibited from Love Push. </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Scamming</strong></span></span></strong></span></p>\n\n<p align="center"><span style="color: #000000;">Love Push has a zero-resistance arrangement on savage conduct of any sort. Anybody endeavoring to get other clients\' private data for false or criminal behavior might be restricted. Any client found sharing their very own budgetary record data (PayPal, Venmo, and so forth.) to get cash from different clients may likewise be prohibited from Love Push. </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Impersonation</strong></span></span></strong></span></p>\n\n<p align="center"><span style="color: #000000;">Act naturally! Try not to claim to be another person. </span></p>\n\n<p align="center"><span style="color: #000000;">Try not to mimic, or generally distort alliance, association or relationship with, any individual or element. This incorporates farce accounts. While we think your Mike Pence profile is diverting, you aren\'t Mike Pence. What\'s more, on the off chance that you are, what are you doing on Love Push? </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Minors</strong></span></span></strong></span></p>\n\n<p align="center"><span style="color: #000000;">You should be 18 years old or more established to utilize Love Push. In that capacity, we don\'t permit pictures of unaccompanied minors. On the off chance that you need to post photographs of your youngsters, if it\'s not too much trouble ensure that you are in the photograph also. On the off chance that you see a profile that incorporates an unaccompanied minor, urges damage to a minor, or delineates a minor in a sexual or intriguing manner, if it\'s not too much trouble report it right away. </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Copyright and Trademark Infringement</strong></span></span></strong></span></p>\n\n<p align="center"><span style="color: #000000;">On the off chance that it\'s not yours, don\'t post it. On the off chance that your Love Push profile incorporates any work that is copyrighted or trademarked by others, don\'t show it, except if you are permitted to do as such. </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Illegal Usage</strong></span></span></strong></span></p>\n\n<p align="center"><span style="color: #000000;">Try not to utilize Love Push to do anything illicit. In the event that it\'s illicit IRL, it\'s unlawful on Love Push. </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>One Person, One Account</strong></span></span></strong></span></p>\n\n<p align="center"><span style="color: #000000;">Love Push records can\'t have different proprietors, so don\'t make a record with your companion or noteworthy other. Furthermore, kindly don\'t keep up different Love Push accounts. </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Third Party Apps</strong></span></span></strong></span></p>\n\n<p align="center"><span style="color: #000000;">The utilization of any applications made by anybody other than Love Push that guarantee to offer our administration or open extraordinary Love Push highlights (like auto-swipers) isn\'t permitted. </span></p>\n\n<p align="center"><span style="color: #000000;"><strong><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;">Account Dormancy</span></span></strong></strong></span></p>\n\n<p align="center"><span style="color: #000000;">Love Push is enjoyable to utilize... constantly! Use Love Push at the lake, use Love Push while eating cake. Use Love Push when you\'re out, use Love Push if all else fails! In any case, on the off chance that you don\'t sign in to your Love Push account in 2 years, we may erase your record for inertia.</span></p>', '2019-09-04 14:50:10', '2019-09-04 14:50:10'),
(2, 'legacy', 'Legacy', '<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>SIGNING UP TERMS AND CONDITIONS</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">Love-Push is a social media app provided as a platform for meeting and connecting with new people, a forum to have discussions and to share images, live streaming, and other information. We designed it to be a fun place to access and visit, and it is paramount to us to make the Love-Push community continually safe and friendly atmosphere, therefore you agree that you will only use Love-Push in ways that are constantly with the purpose which Love-Push was found and in accordance with these governing terms and conditions, as well as the Love-Push Privacy policy and other subordinate terms. For the purpose of these terms, Love-Push, mean the App and all the affiliated applications or websites (Read Privacy Policy).</span></p>\n\n<p align="left"><span style="color: #000000;">These Terms shall be interpreted as a legal agreement between you as user \n("you") and the Love-Push Company ("us" or "we"). </span></p>\n<p align="left"><span style="color: #000000;">These Terms shall apply whenever you access and visit Love-Push, whether or not you have chosen to sign up with us, therefore please read and understand the terms carefully. By visiting, accessing, using, signing up or receiving services offered through Love-Push, you are accepting and consenting to be bound by these Terms. </span></p>\n\n<p align="left"><span style="color: #000000;">IF YOU DISAGREE WITH ANY OR ALL THE TERMS OF THIS APP THEN YOU \nMUST NOT ACCESS OR/AND USE THE APP. </span></p>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>APP USE AND CONTENT POSTING</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;"><strong>Age Limitations:</strong> For an individual to qualify to access and use Love-Push, the \nindividual must be at least 18/21(in some states) years of age. If an individual is above 18/21(in some states) years of age, but below 18/21(in some states) years, it will be required that such individual seek the consent of your parent or legal guardian’s permission to access and use Love-Push and to agree to the Terms and Conditions.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong>Personal Use:</strong> By agreeing to these Terms and Conditions, you consent not to \nuse Love-Push for private business purposes; you can only use the Services for non-commercial, own purposes. </span></p>\n\n<p align="left"><span style="color: #000000;"><strong>Your Conduct:</strong> Don\'t abuse Love-Push. You are permitted to use the Services \nprovided as permitted by the applicable law, including export and re-export control regulations and laws. You are solely liable for your conduct and your content (and information) stored in Love-Push servers, and you must conform to our policy. </span></p>\n\n<p align="left"><span style="color: #000000;"><strong>Your Content:</strong>  You retain the ownership of any property rights that you possess \nthrough any content posted via this service. In actual words, what is yours remains yours. </span></p>\n\n\n<p align="left"><span style="color: #000000;">When you submit, upload, store, send and receive content to or via Love-Push, \nyou give us a worldwide license to host, use, modify reproduce, store, create derivative works (which includes those resulting from adaptations, translations or other changes we make in relation with your content so that they fit perfectly with our Services), publish, communicate, distribute and publicly display or perform such content. </span></p>\n\n<p align="left"><span style="color: #000000;">The license you grant to us is for a limited purpose which is intended for smooth \nrunning, operating, promoting and enhancing our Services and to develop new programs and services. </span></p>\n\n<p align="left"><span style="color: #000000;"><strong>Love-Push Services:</strong>  Using Love-Push does not grant you the right of ownership \nto any intellectual property rights in Love-Push or that of the content you access. You may not use the content you acquired from us unless you obtain the prior written permission from the original owner of the content or you are otherwise permitted to do so by the law. These Terms and Conditions do not allow you or grant you the right to use any logos, branding or any other identity used in Love-Push. You are also not allowed to alter, obscure or remove any legal notices displayed in or in connection with Love-Push. </span></p>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Prohibited Use</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">You are not permitted to use or access the App for any unlawful or unsanctioned purpose apart from the purpose approved and made available by Love-Push. The App is strictly for the user\'s personal use and may not be used in connection with any commercial activities apart from those are sanctioned or approved by Love-Push.</span></p>\n\n<p align="left"><span style="color: #000000;">In case of any prohibited activities involving a user, Love-Push reserves the exclusive right to suspend, terminate, block or deactivate the user\'s profile and prohibit the user for further access and use of the service. All Ads and profiles \nthat violate this section will be deleted by the Support, and the Love Points will NOT be refunded.</span></p>\n\n<p align="left"><span style="color: #000000;">Prohibited activities during live streams and love and connection including but not limited to the following: criminal activity, including child pornography (or molestation), sales or advertisement, racism, insults, swear words, sexism, \nrequests for illegal actions, financial crime, trafficking in obscene material, illegal business, gambling, harassment, stalking, spamming, copyright breach, patent violation, or theft of trade ideas; advertising to, or solicitation of, any user to buy or sell any products or services, unless authorized by Love-Push.</span></p>\n\n<p align="left"><span style="color: #000000;">You consent that the outcome of commercial use of the content or materials found on Love-Push may lead to huge financial compensation that is deemed satisfactory as a remedy and that Love-Push will be entitled to temporary or permanent injunctive relief to prohibit and caution such use.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>USER\'S CONDUCT</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">You consent that under no circumstances shall you post, upload, transmit or host \nany content or ad that:</span></p>\n<ul style="padding-left: 30px;">\n<li align="left"><span style="color: #000000;">is illegal or support illegal activities;</li>\n<li align="left"><span style="color: #000000;">is or contains sexually explicit material;</li>\n<li align="left"><span style="color: #000000;">is defamatory, libelous, religious, racism, fraudulent or personally offencive;</li>\n<li align="left"><span style="color: #000000;">impersonates or imitate a person, brand or company with the intent to confuse, deceive or scam others;</li>\n<li align="left"><span style="color: #000000;">contains any viruses, corrupt files, spyware, worm programmes or other harmful or malicious code intended to damage, interrupt or limit the functionality or effectiveness of or disrupt any hardware, software, \nnetworks, servers, telecommunications, or other Love-Push equipment, Trojan horse or any other content or material constructed to damage, interfere with, wrongly expropriate or intercept any data or personal data whether from Love-Push or otherwise;</li>\n<li align="left"><span style="color: #000000;">is prejudiced or intolerant toward any individual or group;</li>\n<li align="left"><span style="color: #000000;">contains any active or current malware or viruses, or uses the platform for exploits and exploit delivery (such as part of a command and control system); or</li>\n<li align="left"><span style="color: #000000;">Using of abusive language is strictly prohibited.</li>\n</ul>\n\n<p align="left"><span style="color: #000000;">violates any intellectual proprietary right of any party, including trademark, copyright, patent, trade secret, publicity rights, or other rights.	You Account will be suspended if you violates any policy,</span></p>\n\n<p align="left"><span style="color: #000000;"><strong style="font-style:italic;">All such content or ad posted will be deleted and spent LovePoints used in posting won\'t be refunded</strong></span></p>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Intellectual Property Right</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">The content of the App including trademarks, logos, and service marks contained therein (collectively called "Marks") are possessed by or licensed to Love-Push, and are subject to copyright and other intellectual property rights in the United \nArab Emirates and applicable international conventions. All source codes, functionality, databases, software, website design, text, video, audio, images, graphics and photographs (collectively called "Love-Push Content") are providedto the user on "AS IS" basis which is meant for the personal use of the user only. Love-Push Content may not be copied, used, published, reproduced, shared, transmitted, broadcasted, displayed, sold, licensed, or otherwise exploited for any other purposes whatsoever without the prior written consent of Love-Push. Systematic retrieval of information or other details from the App to compile or create, whether directly or indirectly, a compilation, database, collection, or directory without prior written prior permission of Love-Push is hereby prohibited.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>External Links</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">To further serve you better, the App may provide some links that direct to external websites which may or may not be affiliated with Love-Push. Love-Pushis not liable or accountable for the content of any website established and control by any other entity apart from Love-Push. These external websites or apps may collect your personal information and additional information pertaining to your \nonline involvement through the use of web beacons, cookies and other similar technologies in a bid to understand your interests and to deliver to \nadvertisements in accordance with your interests. Please be notified that we do not own the websites, and as such, we do not have access to or exercise any control over the information collected by these external websites or services. The information practices of these external websites are not included in this Privacy Policy.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>LIMITATION OF LIABILITY</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">YOU UNDERSTAND THAT, LOVE-PUSH, ITS, DIRECTORS, AFFILIATES, EMPLOYEES, MEMBERS, OR AGENTS WILL NOT, UNDER ANY SITUATION, BE LIABLE OR RESPONSIBLE FOR INDIRECT DAMAGE, BE IT UNUSUAL, OR CONSEQUENTIAL, WHETHER IN CONTRACT OR AN ACTION INCLUDING \nBUT NOT LIMITED TO LOSS OF DATA, LOSS OF USE AND/OR LOSS OF PROFITS; WHETHER INCLUDING BUT NOT LIMITED TO NEGLIGENCE OR, ARISING FROM USAGE OR LACK OF ABILITY TO USE OUR SERVICES AND PROPRIETARY MATERIALS ACCESSED THROUGH OUR SERVICE.</span></p>\n\n<p align="left"><span style="color: #000000;">YOU CANNOT HOLD US RESPONSIBLE OR LIABLE FOR ANY DAMAGE THAT RESULTS FROM OR IS CAUSED BY RELIANCE ON ANY INFORMATION THAT USERS OBTAIN FROM LOVE-PUSH, OR THAT RESULTS FROM MISTAKES, OMISSIONS, INTERRUPTIONS, DELAYS IN TRANSMISSION OR OPERATION \nOR ANY PERFORMANCE FAILURE, DELETION OF FILES, EMAIL DEFECTS, ERRORS, VIRUSES, WHETHER OR NOT IT RESULTS FROM ACTS OF GOD, \nTHEFT, COMMUNICATIONS FAILURE, DESTRUCTION OR ACCESS BY UNAUTHORIZED ENTITIES, TO DATA, PROGRAMS OR SERVICES, MATERIALS AND CONTENT ON LOVE-PUSH \'S APP.</span></p>\n\n<p align="left"><span style="color: #000000;">LOVE-PUSH\'S CUMULATIVE LIABILITY, IN ANY FORM OF ACTION WHATSOEVER, IN CONNECTION WITH THE USE OF OUR SERVICES OR THIS AGREEMENT WILL NOT EXCEED THE CUMULATIVE AMOUNT OF FEES FOR PAID SERVICES THAT YOU PAID NOT LATER THAN SIX MONTHS - THIS IS \nAPPLICABLE UNDER ANY CIRCUMSTANCE AS IT RELATES WITH THE USAGE OF OUR SERVICES.</span></p>\n\n<p align="left"><span style="color: #000000;">TO THE FULL EXTENT PERMISSIBLE BY RELEVANT JURIDICAL LAWS, AND UNDER NO CIRCUMSTANCE SHALL THE COMPANY BE LIABLE FOR ANY DAMAGES WHATSOEVER, WHETHER COMPENSATORY, GENERAL, SPECIAL, INDIRECT, DIRECT, INCIDENTAL AND/OR CONSEQUENTIAL, THAT RELATES \nTO YOUR USE OF THE SERVICES OR THAT OF THE USERS ON THE PLATFORM, THIS IS INCLUDING BUT WITHOUT LIMITATION TO EMOTIONAL DISTRESS, BODILY INJURY, IDENTITY THEFT AND/OR ANY OTHER DAMAGES THAT MAY POSSIBLY RESULT FROM INTERACTIONS AND COMMUNICATIONS WITH OTHER USERS ON OUR APP AND FROM THE \nCONDUCT OF USERS WHO MAY HAVE REGISTERED UNDER FALSE PRETENSES, INCLUDING THOSE WHO MAY ATTEMPT TO DEFRAUD OR HARM YOU.</span></p>\n\n<p align="left"><span style="color: #000000;">IF YOUR COUNTRY AND JURISDICTION DOES NOT ALLOW THE EXCLUSION OF LIABILITY IN CONTRACTS OR LIMITATION WITH CONSUMERS, THE CONTENTS OF THIS SECTION MAY NOT APPLY TO YOU.</span></p>\n\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>DISCLAIMER AND WARRANTY</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">THE INFORMATION, CONTENT, AND SERVICES AVAILABLE THROUGH THE APP (WHETHER PROVIDED BY LOVE-PUSH, YOU, OTHER USERS OR OTHER THIRD PARTIES/AFFILIATES), INCLUDING WITHOUT LIMITATION, SERVICE REQUESTS SUBMISSIONS, TEXT, PHOTOS, GRAPHICS, AUDIO FILES, VIDEO, AND LINKS, ARE PROVIDED "AS IS" AND "AS AVAILABLE" WITHOUT ANY WARRANTIES WHATSOEVER, EITHER EXPRESS OR IMPLIED. TO THE FULLEST \nEXTENT PERMITTED BY LAW, LOVE-PUSH DISCLAIMS ALL WARRANTIES AND REPRESENTATIONS, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED \nTO WARRANTIES OF MERCHANTABILITY, FITNESS FOR A SPECIFIC PURPOSE, TITLE, NONINFRINGEMENT, FREEDOM FROM COMPUTER VIRUS, AND IMPLIED WARRANTIES ARISING DURING THE COURSE OF DEALING OR COURSE OF PERFORMANCE.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>INDEMNIFICATION</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">You consent to indemnify and hold Love-Push, its affiliates, and subsidiaries, directors, officers, employees, agents or contractors harmless from any claim, damage or demand (direct, indirect or consequential), including reasonable attorney\'s fees, made by anyone in relation to your use of the Site, your submissions, with any purported violation of intellectual property or any other \nrights of any person or body pertaining to the Site, your breach of these Terms and Conditions, and any acts or omissions related to the Site</span></p>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>GOVERNING LAW</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">These Terms and Conditions shall be construed governed and administered by and interpreted in compliance with the laws of Malta. Any dispute arising in relation to these Terms and Conditions shall be subject to the exclusive jurisdiction of the courts situated in Victoria.</span></p>\n\n<p align="left"><span style="color: #000000;">Any dispute or purported claim arising as a result of this App shall be considered\nunder the laws of Malta. Love-Push is domiciled in Malta.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>YOUR USERNAME AND PASSWORD</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">Do not share your log-in details with any third - party. You are the only person authorized to access and/or use your account with Love-Push. It is your duty to protect your password by not sharing your password with anyone or let any third - party access your account as this endangers the security of all of the personal data and content that you have submitted or uploaded to Love-Push. \nIt\'s your sole responsibility to keep your password secure and secret. If you don\'t, Love-Push is not responsible for any illicit access to or use of your account. If you are suspicious that a third - party has gained access to your account, you must contact us immediately through <a href="mailto:support@love-push.com" style="text-decoration:underline;">support@love-push.com</a> to let us know about your concerns or suspicions. You must also change your password as soon as possible. Love-Push has the right to terminate your account or Ad if you violate our terms on keeping your password and log in details secure</span></p>\n\n<p align="left"><span style="color: #000000;">Users shall be entitled to only one account per each user. We reserve the right \nto terminate duplicate accounts. Furthermore, Love Push will not be responsible for any content posted through any user account nor responsible for fake acounts.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>CONTACT INFORMATION</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">Email: <a href="mailto:support@love-push.com" style="text-decoration:underline;">support@love-push.com</a></span></p>\n\n\n<br><br>\n\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>PRIVACY POLICY</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">Thanks for accessing and using Love-Push! Our purpose is to establish a more \nenlightened way of working with our Users to achieve a better platform whereby Users can enjoy the benefits of Love-Push.</span></p>\n\n<p align="left"><span style="color: #000000;">In this Privacy Policy, we define how we collect, use and handle your \ninformation when you submit them to us via our Services.</span></p>\n\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>INFORMATION COLLECTED AND HOW WE USE SUCH INFORMATION</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">We obtain and use the under listed information to provide, enhance and protect \nour Services:</span></p>\n\n<p align="left"><span style="color: #000000;"><strong>Your Personal Information:</strong> Love-Push is a connecting platform for adults, and \nwe provide these services by operating a social network App that allows users the opportunity to connect globally. By joining Love-Push, you enter a legal agreement with us to collect and process your data in order to offer you our service under the terms of this agreement.</span></p>\n\n<p align="left"><span style="color: #000000;">To join the Love-Push network, you will have to complete our online registration \nform, where we may ask you to provide us with information about you such as your name, gender, your date of birth, your email address, your location details,and others as needed. You can also optionally provide other details about yourself. Since you control your user account, these details are at your reach at any time by accessing your "Ad" page, which allows you to update or correct your data at any time by just logging in to Love-Push.</span></p>\n\n<p align="left"><span style="color: #000000;">If you decide to pay for any of our paid services, we will process your payment \ninformation and keep it safe. Under applicable data protection law, it is important for us to collect and use payment data for processing and to assist us in rendering our service to you and our ongoing retention of the data is vindicated by our legitimate interests set out above.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong>Your Content:</strong> Our Services are designed in such a way that it will be simple for \nyou to upload and store your files, photos, messages, comments, etc. ("Your Content"), team up with others, and work across different devices. For this to be realistic, we store, process and transmit Your Content as well as every information that pertains to it. This pertaining information includes the information you have on your profile that makes it easier team up with others and share Your Content, as well as other details such as file size, the time it was uploaded and usage activity.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong>Usage Information:</strong> We collect information in connection with your use of the \nService, including your activities or actions you take in your account (such as streaming, viewing, editing, and moving folders or files). This information is used to strengthen and improve our Services, create new features and service, and to protect Love-Push Users. Please contact us for more information about how we use this information to enhance our Services.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong>Device information.</strong> We also collect information from and about the devices you \nuse to access and use the Services of Love-Push. This includes details such as IP address, the type of device and browser you use, the web page(s) you visited before coming to our sites, and the unique device identifier. Your tools(depending on their settings) may also transmit your location information to the Services.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong>Cookies and other technologies.</strong> We use technologies like cookies, web beacons, \netc. to provide, enhance, support, protect, and promote our Services. When you visit the section made available on the App, a session ID cookie is \nplaced on your browsing device that will only last through your visiting moment. Love-Push will also place persistent cookies (also referred to as local storage devices) on user’s computers so that Love-Push remembers users and enhance your login experience and make it faster and easier. We may also place persistent tracking cookies on your browsing device, solely for protection and security purposes, so as to prevent scammers, unauthorized login attempts, phishers, and also to help you gain access into your hacked account. However, we do not use any of this information while you logged-off the App</span></p>\n\n<p align="left"><span style="color: #000000;">You can still also set your browser not to accept cookies, but this may limit your \nability to use the Services to the fullest. Following Cookies are Used by Us.</span></p>\n\n<ul style="padding-left: 30px;">\n<li align="left"><span style="color: #000000;">XSRF-TOKEN</li>\n<li align="left"><span style="color: #000000;">love_push_session</li>\n\n</ul>\n\n<p align="left"><span style="color: #000000;"><strong>Bases for processing your information:</strong> We collect and use the personal \ninformation and all other information you submit to us as described above toprovide you the Services most securely and reliably. We also use your details for or legitimate business needs. If we process your details for any other reason not stated herein, we will seek your approval before the time; we will also mandate our service providers to do likewise. For more information about the bases for processing your details, please contact us.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>INFORMATION SHARING</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">Since we will not sell, rent or give your information to advertisers or other third \nparties, we may decide to share your information as described below:</span></p>\n\n<p align="left"><span style="color: #000000;"><strong>Our relevant service providers:</strong> Love-Push makes use of some trusted service \nproviders (for instance, for the transaction, customer support and IT management) to assist us in providing, enhancing, protecting and promoting our \nServices. These service providers will have access to your information for the singular purpose of performing their duties to Love-Push in agreement with this Privacy Policy. We will remain liable for the service provider’s handling and management of any of the information they access through our service. For the list of the service providers which we use to process your personal information, please contact us.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong>Other users:</strong> Our Services display your information such as your name, profile \npicture and email address to other users. You can also share Your Content with other users if you so desire. When you register or subscribe on Love-Push account with an email address on a domain owned by your company, we may assist collaborators to find you by disclosing some of your necessary information (your name, email address and profile picture) to others.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong>Law, Order and the general interest of the public:</strong> We may disclose or share your \ninformation with a third party if we find out that such disclosure or sharing is \nessential and necessary to:</span></p>\n\n<ul style="padding-left: 20px;">\n<li align="left" style="list-style:none;"><span style="color: #000000;list-style:none;">i. to conform with any applicable rule, regulation or law;</li>\n<li align="left" style="list-style:none;"><span style="color: #000000;list-style:none;">ii. prevent death or serious personal injury;</li>\n<li align="left" style="list-style:none;"><span style="color: #000000;list-style:none;">iii. prevent illegal or unsanctioned transactions of Love-Push or our Users;</li>\n<li align="left" style="list-style:none;"><span style="color: #000000;list-style:none;">iv. secure Love-Push’s intellectual property rights, interest or safety; or</li>\n<li align="left" style="list-style:none;"><span style="color: #000000;list-style:none;">v. carry out a task in the benefit of the general public.</li>\n\n</ul>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>HOW WE SHARE YOUR INFORMATION</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;"><strong>Security:</strong> We have a team committed to securing your information and checking \nfor weakness in the Services and Site. We also continue to enhance our features to keep your data safe such as encryption of files at rest, and alerts when new devices and apps are linked or connected to your account. We employ the use of the latest automated technologies to detect any misuse of the Services and content that may harm our Services, you, or other users.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong>User Controls:</strong>  You can access, view, modify, download, or delete your personal \ninformation by logging into your Love-Push account and going to your account setting page. You can also restrict the way we collect and use your information on the user control page. To learn more about managing your account\'s information generally, please contact us.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong>Information Retention:</strong>  When you create an account, or you subscribe to our \nServices, we will retain your information for as long as your account is active or for as long as we have to provide the Service to you.</span></p>\n<p align="left"><span style="color: #000000;">If you decide to disable your account, please be informed that there might be \nsome cautions in deleting your information from all our servers and backup database. Be advised that we may retain your information for as long as \nnecessary in compliance with all legal obligations, enforce our agreements or resolve disputes.</span></p>\n\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>DATA PROTECTION</strong></span></span></strong></span></p>\n\n\n<p align="left"><span style="color: #000000;">No data on the internet can be guaranteed as secure or safe. We strive to \nsafeguard information but are unable to ensure and cannot warrant the security of any data which you share on the platform or transmit to us. You understand that any information transmitted, submitted or posted on our platform is done at your own risk. Nevertheless, we will take reasonable steps to ensure the security information on our APP.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>YOUR RIGHT TO ACCESS AND CONTROL YOUR INFORMATION</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">You have absolute control over your personal information and how it is \ncollected, used, and shared. For instance, you have a right to:</span></p>\n\n\n<ul style="padding-left: 30px;">\n<li align="left"><span style="color: #000000;">edit or correct personal data. You can manage your account and the \ncontent contained in it, as well as edit some of your personal details.</li>\n</ul>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>CHANGES TO THIS PRIVACY POLICY</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">If there is a chance that we are involved in a merger, acquisition, reorganization, \nor sale of our assets, your information may be transferred as part of the deal. We will promptly notify you via the email associated with your account/profile of any such occurrence and outline your choices to you.</span></p>\n\n<p align="left"><span style="color: #000000;">This Privacy Policy is subject to change frequently. We will post the most recent \nversion on our Site. If the version materially reduces your rights, we will send a notification to you.</span></p>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>CONTACT INFORMATION</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">Email: <a style="text-decoration:underline;" href="mailto:support@love-push.com">support@love-push.com</a></span></p>\n\n<br><br>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>SUBSCRIPTION TERMS AND CONDITIONS</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">These Terms shall apply whenever you subscribe for any of our paid services \nthough Love-Push App, therefore please read and understand the terms carefully. By subscribing for any paid services offered through Love-Push, you are accepting and consenting to be bound by the following Terms.</span></p>\n\n<ul style="padding-left: 30px;">\n<li align="left"><span style="color: #000000;">By subscribing for any of our paid packages, the Love-Push have the right \nto deduct any total amount due from the user through any added payment method and have the right to sue any user with an insufficient fund to pay for \nthe amount due to his/her subscribed account</li>\n<li align="left"><span style="color: #000000;">All purchases and contracts made through the App for any package come\nwith a two - weeks cancellation policy.</li>\n<li align="left"><span style="color: #000000;">Non-canceled and every subscription shall be automatically renewed for \nthe preceding month from the 7th day before the end of each month</li>\n<li align="left"><span style="color: #000000;">Every subscription shall be cancellable monthly until one week before the \nend of the month.</li>\n\n</ul>\n\n<p align="left"><span style="color: #000000;">These subscription Terms and Conditions shall be construed governed and\nadministered by and interpreted in compliance with the laws of Malta. Any \ndispute arising in relation to these Terms and Conditions shall be subject to the \nexclusive jurisdiction of the courts situated in Victoria.\n</p>\n<p align="left"><span style="color: #000000;">\nAny dispute or purported claim arising as a result of your subscription through \nthe App shall be considered under the laws of Malta. Love-Push is domiciled in \nMalta</span></p>\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>NETWORK MARKETING/AFFILIATE TERMS AND CONDITIONS</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">These Terms shall apply whenever you sign - up for Love-Push Affiliate program, \ntherefore please read and understand the terms carefully. By signing up as Love-Push Affiliate, you are accepting and consenting to be bound by the following Terms.</span></p>\n\n<ul style="padding-left: 30px;">\n<li align="left"><span style="color: #000000;">Every affiliate user of Love-Push must be over 18/21(in some states) years old</li>\n<li align="left"><span style="color: #000000;">Affiliate commissions are paid with taxes.</li>\n<li align="left"><span style="color: #000000;">Every affiliate shall be responsible for any taxes that may be incurred when \npaying out on his PayPal account.</li>\n\n\n</ul>\n\n<p align="left"><span style="color: #000000;">Every user shall be able to see the team/downline plan up to the 5 level with \nusername, also the team turnover, no matter what plan the user subscription is \neither free, gold or VIP pass.</span></p>\n\n<p align="left"><span style="color: #000000;">However, only if the user agrees to the terms and conditions of the affiliate \nprogram & has active V.I.P. Pass shall have active Partner/Affiliate status, and\nstart receives commissions as follows: (with Free Pass users missed commissions \nare shown)</span></p>\n\n\n<p align="left"><span style="color: #000000;"><strong><span style="font-family: proxima-nova, \'Helvetica Neue\', helvetica, sans-serif;"><span style="font-size: large;"><strong>Unilevelplan up to 5 levels 35% commission in total</strong></span></span></strong></span></p>\n\n<p align="left"><span style="color: #000000;">Only achievable with Agreement to this Affiliate Terms and Conditions with \nProgram & active V.I.P. Pass</span></p>\n\n<ul style="padding-left: 20px;">\n<li align="left" style="list-style:none;"><span style="color: #000000;list-style:none;">1. Level: 20%</li>\n<li align="left" style="list-style:none;"><span style="color: #000000;list-style:none;">2. Level: 5%</li>\n<li align="left" style="list-style:none;"><span style="color: #000000;list-style:none;">3. Level: 3%</li>\n<li align="left" style="list-style:none;"><span style="color: #000000;list-style:none;">4. Level: 2%</li>\n<li align="left" style="list-style:none;"><span style="color: #000000;list-style:none;">5. Level: 5%</li>\n\n</ul>\n\n<p align="left"><span style="color: #000000;">Withdraw per Paypal once in a month and on the first day of each month \nwith a minimum of $25 balance of your back office. To request at back office\nuntil 25. each pre - month.</span></p>\n\n<p align="left"><span style="color: #000000;">These Affiliate Terms and Conditions shall be construed governed and \nadministered by and interpreted in compliance with the laws of Malta. Any \ndispute arising in relation to these Terms and Conditions shall be subject to the \nexclusive jurisdiction of the courts situated in Victoria.</span></p>\n\n<p align="left"><span style="color: #000000;">Any dispute or purported claim arising as a result of your subscription through \nthe App shall be considered under the laws of Malta. Love-Push is domiciled in \nMalta.</span></p>', '2019-09-04 14:50:10', '2019-09-04 14:50:10');

-- --------------------------------------------------------

--
-- Table structure for table `subscription_plans`
--

CREATE TABLE `subscription_plans` (
  `id` int(11) NOT NULL,
  `type` enum('F','P','V') NOT NULL COMMENT 'F=free, P=premium, V=vip',
  `name` varchar(255) NOT NULL DEFAULT '',
  `price` float(11,2) NOT NULL DEFAULT '0.00',
  `no_of_chat_req` varchar(255) DEFAULT NULL COMMENT 'per day',
  `no_of_likes` varchar(255) DEFAULT NULL COMMENT 'per day',
  `no_of_view_stories` varchar(255) DEFAULT NULL COMMENT 'per day',
  `status` smallint(1) NOT NULL DEFAULT '1' COMMENT '1=active, 0=inactive',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `subscription_plans`
--

INSERT INTO `subscription_plans` (`id`, `type`, `name`, `price`, `no_of_chat_req`, `no_of_likes`, `no_of_view_stories`, `status`, `created_at`, `updated_at`) VALUES
(1, 'F', 'Free', 0.00, '2', '21', '5', 1, '2019-07-29 12:25:07', '2019-08-02 06:15:59'),
(2, 'P', 'Premium', 4.90, '5', '50', '50', 1, '2019-07-29 12:25:07', '2019-08-02 06:55:02'),
(3, 'V', 'VIP', 9.90, NULL, NULL, NULL, 1, '2019-07-29 12:25:07', '2019-07-29 13:12:28');

-- --------------------------------------------------------

--
-- Table structure for table `support`
--

CREATE TABLE `support` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `subject` varchar(255) NOT NULL,
  `message` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `support`
--

INSERT INTO `support` (`id`, `name`, `subject`, `message`, `created_at`, `updated_at`) VALUES
(1, 'please ignore', 'testing subject', 'please ignore this message.', '2021-04-02 12:13:42', '2021-04-02 17:43:42');

-- --------------------------------------------------------

--
-- Table structure for table `test_ques`
--

CREATE TABLE `test_ques` (
  `id` int(11) NOT NULL,
  `test_id` int(11) NOT NULL,
  `ques_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `test_ques`
--

INSERT INTO `test_ques` (`id`, `test_id`, `ques_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4),
(5, 1, 5),
(6, 1, 6),
(7, 1, 7),
(8, 1, 13),
(9, 2, 1),
(10, 2, 2),
(11, 2, 3),
(12, 2, 4),
(13, 2, 5),
(14, 2, 6),
(15, 2, 7),
(16, 2, 13),
(35, 5, 1),
(36, 5, 2),
(37, 5, 3),
(38, 5, 4),
(39, 5, 5),
(40, 5, 6),
(41, 5, 7),
(42, 5, 13),
(51, 7, 1),
(52, 7, 2),
(53, 7, 3),
(54, 7, 4),
(55, 7, 5),
(56, 7, 6),
(57, 7, 7),
(58, 7, 13),
(59, 7, 15),
(60, 7, 16),
(110, 14, 1),
(111, 14, 2),
(112, 14, 3),
(113, 14, 4),
(114, 14, 5),
(115, 14, 6),
(116, 14, 7),
(117, 14, 13),
(118, 14, 15),
(121, 14, 16),
(163, 20, 1),
(164, 21, 1),
(165, 20, 2),
(166, 21, 2),
(167, 20, 3),
(168, 21, 3),
(169, 20, 4),
(170, 21, 4),
(171, 20, 5),
(172, 21, 5),
(173, 20, 6),
(174, 21, 6),
(175, 20, 7),
(176, 21, 7),
(177, 20, 13),
(178, 21, 13),
(179, 21, 15),
(180, 20, 15),
(181, 20, 16),
(182, 21, 16),
(183, 22, 1),
(184, 22, 2),
(185, 22, 3),
(186, 22, 4),
(187, 22, 5),
(188, 22, 6),
(189, 22, 7),
(190, 22, 13),
(191, 22, 15),
(192, 22, 16),
(213, 25, 1),
(214, 25, 2),
(215, 25, 3),
(216, 25, 4),
(217, 25, 5),
(218, 25, 6),
(219, 25, 7),
(220, 25, 13),
(221, 25, 15),
(222, 25, 16),
(226, 29, 1),
(227, 30, 1),
(228, 31, 1),
(229, 31, 2),
(230, 31, 3),
(231, 31, 4),
(232, 31, 5),
(233, 31, 6),
(234, 31, 7),
(235, 31, 13),
(236, 32, 1),
(237, 32, 2),
(238, 32, 3),
(239, 32, 4),
(240, 32, 5),
(241, 32, 6),
(242, 32, 7),
(243, 32, 13),
(244, 33, 1),
(245, 33, 2),
(246, 33, 3),
(247, 33, 4),
(248, 33, 5),
(249, 33, 6),
(250, 33, 7),
(251, 33, 13),
(252, 33, 15),
(253, 33, 16),
(254, 34, 1),
(255, 34, 2),
(256, 34, 3),
(257, 34, 4),
(258, 34, 5),
(259, 34, 6),
(260, 34, 7),
(261, 34, 13),
(262, 35, 1),
(263, 35, 2),
(264, 35, 3),
(265, 35, 4),
(266, 35, 5),
(267, 35, 6),
(268, 35, 7),
(269, 35, 13),
(270, 36, 1),
(271, 36, 2),
(272, 36, 3),
(273, 36, 4),
(274, 36, 5),
(275, 36, 6),
(276, 36, 7),
(277, 36, 13),
(278, 37, 1),
(279, 37, 2),
(280, 37, 3),
(281, 37, 4),
(282, 37, 5),
(283, 37, 6),
(284, 37, 7),
(285, 37, 13),
(286, 37, 15),
(287, 37, 16),
(288, 38, 1),
(289, 38, 2),
(290, 38, 3),
(291, 38, 4),
(292, 38, 5),
(293, 38, 6),
(294, 38, 7),
(295, 38, 13);

-- --------------------------------------------------------

--
-- Table structure for table `test_ques_ans`
--

CREATE TABLE `test_ques_ans` (
  `id` int(11) NOT NULL,
  `test_que_id` int(11) NOT NULL,
  `ans_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `test_ques_ans`
--

INSERT INTO `test_ques_ans` (`id`, `test_que_id`, `ans_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 5),
(4, 3, 10),
(5, 4, 15),
(6, 5, 17),
(7, 5, 18),
(8, 6, 23),
(9, 6, 24),
(10, 7, 35),
(11, 7, 34),
(12, 7, 33),
(13, 8, 68),
(14, 8, 69),
(15, 8, 70),
(16, 9, 2),
(17, 10, 5),
(18, 11, 12),
(19, 12, 16),
(20, 13, 17),
(21, 13, 20),
(22, 14, 22),
(23, 14, 27),
(24, 15, 31),
(25, 15, 30),
(26, 15, 34),
(27, 16, 65),
(28, 16, 68),
(29, 16, 70),
(61, 35, 1),
(62, 36, 5),
(63, 37, 9),
(64, 38, 14),
(65, 39, 17),
(66, 39, 19),
(67, 40, 23),
(68, 40, 25),
(69, 41, 32),
(70, 41, 33),
(71, 41, 34),
(72, 42, 66),
(73, 42, 67),
(74, 42, 65),
(89, 51, 2),
(90, 52, 4),
(91, 53, 9),
(92, 54, 13),
(93, 55, 18),
(94, 55, 19),
(95, 56, 24),
(96, 57, 33),
(97, 58, 68),
(98, 59, 83),
(99, 60, 89),
(168, 110, 1),
(169, 110, 2),
(170, 111, 4),
(171, 112, 9),
(172, 113, 13),
(173, 114, 17),
(174, 115, 25),
(175, 116, 33),
(176, 117, 65),
(177, 117, 68),
(178, 118, 85),
(181, 121, 89),
(228, 163, 1),
(229, 163, 2),
(230, 164, 1),
(231, 164, 2),
(232, 165, 4),
(233, 166, 4),
(234, 167, 9),
(235, 168, 9),
(236, 169, 13),
(237, 170, 13),
(238, 171, 17),
(239, 171, 21),
(240, 172, 17),
(241, 172, 21),
(242, 173, 24),
(243, 173, 25),
(244, 174, 24),
(245, 174, 25),
(246, 175, 33),
(247, 175, 34),
(248, 175, 35),
(249, 176, 33),
(250, 176, 34),
(251, 176, 35),
(252, 177, 65),
(253, 177, 67),
(254, 177, 68),
(255, 178, 65),
(256, 178, 67),
(257, 178, 68),
(258, 179, 80),
(259, 179, 85),
(260, 179, 84),
(261, 180, 80),
(262, 180, 84),
(263, 180, 85),
(264, 181, 89),
(265, 182, 89),
(266, 183, 1),
(267, 183, 2),
(268, 184, 4),
(269, 185, 9),
(270, 186, 13),
(271, 187, 18),
(272, 187, 20),
(273, 188, 22),
(274, 188, 23),
(275, 189, 31),
(276, 190, 69),
(277, 191, 81),
(278, 192, 88),
(299, 213, 1),
(300, 213, 3),
(301, 213, 2),
(302, 214, 8),
(303, 215, 10),
(304, 216, 13),
(305, 217, 20),
(306, 218, 28),
(307, 218, 25),
(308, 219, 30),
(309, 219, 37),
(310, 220, 72),
(311, 221, 81),
(312, 222, 88),
(320, 226, 1),
(321, 227, 1),
(322, 227, 3),
(323, 228, 1),
(324, 229, 7),
(325, 230, 10),
(326, 231, 13),
(327, 232, 17),
(328, 232, 20),
(329, 233, 25),
(330, 233, 24),
(331, 234, 34),
(332, 234, 37),
(333, 235, 69),
(334, 236, 2),
(335, 237, 5),
(336, 238, 9),
(337, 239, 16),
(338, 240, 17),
(339, 241, 27),
(340, 241, 28),
(341, 242, 36),
(342, 242, 32),
(343, 242, 33),
(344, 243, 67),
(345, 243, 68),
(346, 243, 65),
(347, 244, 2),
(348, 245, 8),
(349, 246, 11),
(350, 247, 15),
(351, 248, 20),
(352, 249, 23),
(353, 249, 24),
(354, 250, 37),
(355, 250, 30),
(356, 250, 33),
(357, 251, 65),
(358, 251, 66),
(359, 251, 67),
(360, 252, 85),
(361, 253, 89),
(362, 254, 2),
(363, 255, 6),
(364, 256, 12),
(365, 257, 13),
(366, 258, 17),
(367, 258, 19),
(368, 259, 22),
(369, 259, 26),
(370, 260, 30),
(371, 260, 36),
(372, 260, 37),
(373, 261, 65),
(374, 261, 67),
(375, 261, 68),
(376, 262, 2),
(377, 263, 5),
(378, 264, 9),
(379, 265, 15),
(380, 266, 18),
(381, 267, 24),
(382, 268, 31),
(383, 268, 32),
(384, 268, 33),
(385, 269, 66),
(386, 269, 67),
(387, 270, 1),
(388, 271, 5),
(389, 272, 10),
(390, 273, 13),
(391, 274, 19),
(392, 275, 24),
(393, 275, 25),
(394, 276, 33),
(395, 276, 32),
(396, 277, 68),
(397, 277, 69),
(398, 277, 67),
(399, 278, 2),
(400, 279, 4),
(401, 280, 10),
(402, 281, 14),
(403, 282, 19),
(404, 283, 24),
(405, 283, 23),
(406, 284, 32),
(407, 284, 33),
(408, 285, 65),
(409, 286, 82),
(410, 286, 83),
(411, 287, 88),
(412, 288, 2),
(413, 288, 1),
(414, 289, 5),
(415, 290, 12),
(416, 291, 14),
(417, 292, 19),
(418, 293, 23),
(419, 294, 32),
(420, 294, 33),
(421, 295, 66);

-- --------------------------------------------------------

--
-- Table structure for table `test_ques_comparisons`
--

CREATE TABLE `test_ques_comparisons` (
  `id` int(11) NOT NULL,
  `test_id_1` int(11) DEFAULT NULL COMMENT 'user_id 1',
  `test_id_2` int(11) DEFAULT NULL COMMENT 'user_id 2',
  `test_que_1` int(11) DEFAULT NULL,
  `test_que_2` int(11) DEFAULT NULL,
  `obtained_scores` decimal(10,2) NOT NULL,
  `max_allowed_scores` decimal(10,2) NOT NULL,
  `final_scores` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `test_ques_comparisons`
--

INSERT INTO `test_ques_comparisons` (`id`, `test_id_1`, `test_id_2`, `test_que_1`, `test_que_2`, `obtained_scores`, `max_allowed_scores`, `final_scores`) VALUES
(1, 2, 1, 9, 1, '0.00', '0.00', '0.00'),
(2, 2, 1, 10, 2, '0.00', '0.00', '0.00'),
(3, 2, 1, 11, 3, '10.00', '20.00', '10.00'),
(4, 2, 1, 12, 4, '10.00', '20.00', '10.00'),
(5, 2, 1, 13, 5, '60.00', '80.00', '60.00'),
(6, 2, 1, 14, 6, '10.00', '40.00', '10.00'),
(7, 2, 1, 15, 7, '30.00', '40.00', '30.00'),
(8, 2, 1, 16, 8, '45.00', '60.00', '45.00'),
(49, 5, 1, 35, 1, '0.00', '0.00', '0.00'),
(50, 5, 1, 36, 2, '0.00', '0.00', '0.00'),
(51, 5, 1, 37, 3, '5.00', '20.00', '5.00'),
(52, 5, 1, 38, 4, '0.00', '20.00', '0.00'),
(53, 5, 1, 39, 5, '40.00', '80.00', '40.00'),
(54, 5, 1, 40, 6, '25.00', '40.00', '25.00'),
(55, 5, 1, 41, 7, '45.00', '40.00', '40.00'),
(56, 5, 1, 42, 8, '15.00', '60.00', '15.00'),
(57, 5, 2, 35, 9, '0.00', '0.00', '0.00'),
(58, 5, 2, 36, 10, '0.00', '0.00', '0.00'),
(59, 5, 2, 37, 11, '5.00', '20.00', '5.00'),
(60, 5, 2, 38, 12, '5.00', '20.00', '5.00'),
(61, 5, 2, 39, 13, '40.00', '80.00', '40.00'),
(62, 5, 2, 40, 14, '10.00', '40.00', '10.00'),
(63, 5, 2, 41, 15, '35.00', '40.00', '35.00'),
(64, 5, 2, 42, 16, '30.00', '60.00', '30.00'),
(97, 7, 1, 51, 1, '0.00', '0.00', '0.00'),
(98, 7, 1, 52, 2, '0.00', '0.00', '0.00'),
(99, 7, 1, 53, 3, '5.00', '20.00', '5.00'),
(100, 7, 1, 54, 4, '10.00', '20.00', '10.00'),
(101, 7, 1, 55, 5, '40.00', '80.00', '40.00'),
(102, 7, 1, 56, 6, '20.00', '40.00', '20.00'),
(103, 7, 1, 57, 7, '20.00', '40.00', '20.00'),
(104, 7, 1, 58, 8, '20.00', '60.00', '20.00'),
(105, 7, 2, 51, 9, '0.00', '0.00', '0.00'),
(106, 7, 2, 52, 10, '0.00', '0.00', '0.00'),
(107, 7, 2, 53, 11, '5.00', '20.00', '5.00'),
(108, 7, 2, 54, 12, '5.00', '20.00', '5.00'),
(109, 7, 2, 55, 13, '0.00', '80.00', '0.00'),
(110, 7, 2, 56, 14, '5.00', '40.00', '5.00'),
(111, 7, 2, 57, 15, '5.00', '40.00', '5.00'),
(112, 7, 2, 58, 16, '20.00', '60.00', '20.00'),
(113, 7, 5, 51, 35, '0.00', '0.00', '0.00'),
(114, 7, 5, 52, 36, '0.00', '0.00', '0.00'),
(115, 7, 5, 53, 37, '20.00', '20.00', '20.00'),
(116, 7, 5, 54, 38, '0.00', '20.00', '0.00'),
(117, 7, 5, 55, 39, '40.00', '80.00', '40.00'),
(118, 7, 5, 56, 40, '10.00', '40.00', '10.00'),
(119, 7, 5, 57, 41, '20.00', '40.00', '20.00'),
(120, 7, 5, 58, 42, '5.00', '60.00', '5.00'),
(356, 14, 1, 110, 1, '0.00', '0.00', '0.00'),
(357, 14, 1, 111, 2, '0.00', '0.00', '0.00'),
(358, 14, 1, 112, 3, '5.00', '20.00', '5.00'),
(359, 14, 1, 113, 4, '10.00', '20.00', '10.00'),
(360, 14, 1, 114, 5, '40.00', '80.00', '40.00'),
(361, 14, 1, 115, 6, '5.00', '40.00', '5.00'),
(362, 14, 1, 116, 7, '20.00', '40.00', '20.00'),
(363, 14, 1, 117, 8, '25.00', '60.00', '25.00'),
(364, 14, 2, 110, 9, '0.00', '0.00', '0.00'),
(365, 14, 2, 111, 10, '0.00', '0.00', '0.00'),
(366, 14, 2, 112, 11, '5.00', '20.00', '5.00'),
(367, 14, 2, 113, 12, '5.00', '20.00', '5.00'),
(368, 14, 2, 114, 13, '40.00', '80.00', '40.00'),
(369, 14, 2, 115, 14, '10.00', '40.00', '10.00'),
(370, 14, 2, 116, 15, '5.00', '40.00', '5.00'),
(371, 14, 2, 117, 16, '45.00', '60.00', '45.00'),
(372, 14, 5, 110, 35, '0.00', '0.00', '0.00'),
(373, 14, 5, 111, 36, '0.00', '0.00', '0.00'),
(374, 14, 5, 112, 37, '20.00', '20.00', '20.00'),
(375, 14, 5, 113, 38, '0.00', '20.00', '0.00'),
(376, 14, 5, 114, 39, '40.00', '80.00', '40.00'),
(377, 14, 5, 115, 40, '20.00', '40.00', '20.00'),
(378, 14, 5, 116, 41, '20.00', '40.00', '20.00'),
(379, 14, 5, 117, 42, '25.00', '60.00', '25.00'),
(388, 14, 7, 110, 51, '0.00', '0.00', '0.00'),
(389, 14, 7, 111, 52, '0.00', '0.00', '0.00'),
(390, 14, 7, 112, 53, '20.00', '20.00', '20.00'),
(391, 14, 7, 113, 54, '20.00', '20.00', '20.00'),
(392, 14, 7, 114, 55, '0.00', '80.00', '0.00'),
(393, 14, 7, 115, 56, '5.00', '40.00', '5.00'),
(394, 14, 7, 116, 57, '20.00', '40.00', '20.00'),
(395, 14, 7, 117, 58, '20.00', '60.00', '20.00'),
(415, 14, 7, 118, 59, '5.00', '60.00', '5.00'),
(416, 14, 7, 121, 60, '20.00', '20.00', '20.00'),
(685, 20, 1, 163, 1, '0.00', '0.00', '0.00'),
(686, 20, 1, 165, 2, '0.00', '0.00', '0.00'),
(687, 20, 1, 167, 3, '5.00', '20.00', '5.00'),
(688, 20, 1, 169, 4, '10.00', '20.00', '10.00'),
(689, 20, 1, 171, 5, '40.00', '80.00', '40.00'),
(690, 20, 1, 173, 6, '25.00', '40.00', '25.00'),
(691, 20, 1, 175, 7, '60.00', '40.00', '40.00'),
(692, 20, 1, 177, 8, '30.00', '60.00', '30.00'),
(693, 20, 2, 163, 9, '0.00', '0.00', '0.00'),
(694, 20, 2, 165, 10, '0.00', '0.00', '0.00'),
(695, 20, 2, 167, 11, '5.00', '20.00', '5.00'),
(696, 20, 2, 169, 12, '5.00', '20.00', '5.00'),
(697, 20, 2, 171, 13, '40.00', '80.00', '40.00'),
(698, 20, 2, 173, 14, '10.00', '40.00', '10.00'),
(699, 20, 2, 175, 15, '30.00', '40.00', '30.00'),
(700, 20, 2, 177, 16, '45.00', '60.00', '45.00'),
(701, 20, 5, 163, 35, '0.00', '0.00', '0.00'),
(702, 20, 5, 165, 36, '0.00', '0.00', '0.00'),
(703, 20, 5, 167, 37, '20.00', '20.00', '20.00'),
(704, 20, 5, 169, 38, '0.00', '20.00', '0.00'),
(705, 21, 1, 164, 1, '0.00', '0.00', '0.00'),
(706, 20, 5, 171, 39, '40.00', '80.00', '40.00'),
(707, 21, 1, 166, 2, '0.00', '0.00', '0.00'),
(708, 20, 5, 173, 40, '30.00', '40.00', '30.00'),
(709, 21, 1, 168, 3, '5.00', '20.00', '5.00'),
(710, 20, 5, 175, 41, '45.00', '40.00', '40.00'),
(711, 21, 1, 170, 4, '10.00', '20.00', '10.00'),
(712, 20, 5, 177, 42, '45.00', '60.00', '45.00'),
(713, 21, 1, 172, 5, '40.00', '80.00', '40.00'),
(714, 21, 1, 174, 6, '25.00', '40.00', '25.00'),
(716, 21, 1, 176, 7, '60.00', '40.00', '40.00'),
(718, 21, 1, 178, 8, '30.00', '60.00', '30.00'),
(721, 21, 2, 164, 9, '0.00', '0.00', '0.00'),
(723, 21, 2, 166, 10, '0.00', '0.00', '0.00'),
(725, 21, 2, 168, 11, '5.00', '20.00', '5.00'),
(727, 21, 2, 170, 12, '5.00', '20.00', '5.00'),
(729, 21, 2, 172, 13, '40.00', '80.00', '40.00'),
(730, 21, 2, 174, 14, '10.00', '40.00', '10.00'),
(731, 20, 7, 163, 51, '0.00', '0.00', '0.00'),
(732, 21, 2, 176, 15, '30.00', '40.00', '30.00'),
(733, 20, 7, 165, 52, '0.00', '0.00', '0.00'),
(734, 21, 2, 178, 16, '45.00', '60.00', '45.00'),
(735, 20, 7, 167, 53, '20.00', '20.00', '20.00'),
(736, 20, 7, 169, 54, '20.00', '20.00', '20.00'),
(737, 21, 5, 164, 35, '0.00', '0.00', '0.00'),
(738, 20, 7, 171, 55, '0.00', '80.00', '0.00'),
(739, 21, 5, 166, 36, '0.00', '0.00', '0.00'),
(740, 20, 7, 173, 56, '20.00', '40.00', '20.00'),
(741, 21, 5, 168, 37, '20.00', '20.00', '20.00'),
(742, 20, 7, 175, 57, '20.00', '40.00', '20.00'),
(743, 21, 5, 170, 38, '0.00', '20.00', '0.00'),
(744, 20, 7, 177, 58, '20.00', '60.00', '20.00'),
(745, 21, 5, 172, 39, '40.00', '80.00', '40.00'),
(746, 21, 5, 174, 40, '30.00', '40.00', '30.00'),
(747, 20, 14, 163, 110, '0.00', '0.00', '0.00'),
(748, 21, 5, 176, 41, '45.00', '40.00', '40.00'),
(749, 20, 14, 165, 111, '0.00', '0.00', '0.00'),
(750, 21, 5, 178, 42, '45.00', '60.00', '45.00'),
(751, 20, 14, 167, 112, '20.00', '20.00', '20.00'),
(752, 20, 14, 169, 113, '20.00', '20.00', '20.00'),
(754, 20, 14, 171, 114, '40.00', '80.00', '40.00'),
(756, 20, 14, 173, 115, '20.00', '40.00', '20.00'),
(758, 20, 14, 175, 116, '20.00', '40.00', '20.00'),
(760, 20, 14, 177, 117, '40.00', '60.00', '40.00'),
(769, 21, 7, 164, 51, '0.00', '0.00', '0.00'),
(771, 21, 7, 166, 52, '0.00', '0.00', '0.00'),
(773, 21, 7, 168, 53, '20.00', '20.00', '20.00'),
(775, 21, 7, 170, 54, '20.00', '20.00', '20.00'),
(777, 21, 7, 172, 55, '0.00', '80.00', '0.00'),
(778, 21, 7, 174, 56, '20.00', '40.00', '20.00'),
(779, 20, 21, 163, 164, '0.00', '0.00', '0.00'),
(780, 21, 7, 176, 57, '20.00', '40.00', '20.00'),
(781, 20, 21, 165, 166, '0.00', '0.00', '0.00'),
(782, 21, 7, 178, 58, '20.00', '60.00', '20.00'),
(783, 20, 21, 167, 168, '20.00', '20.00', '20.00'),
(784, 20, 21, 169, 170, '20.00', '20.00', '20.00'),
(785, 21, 14, 164, 110, '0.00', '0.00', '0.00'),
(786, 20, 21, 171, 172, '80.00', '80.00', '80.00'),
(787, 21, 14, 166, 111, '0.00', '0.00', '0.00'),
(788, 20, 21, 173, 174, '40.00', '40.00', '40.00'),
(789, 21, 14, 168, 112, '20.00', '20.00', '20.00'),
(790, 20, 21, 175, 176, '60.00', '40.00', '40.00'),
(791, 21, 14, 170, 113, '20.00', '20.00', '20.00'),
(792, 21, 14, 172, 114, '40.00', '80.00', '40.00'),
(793, 21, 14, 174, 115, '20.00', '40.00', '20.00'),
(794, 21, 14, 176, 116, '20.00', '40.00', '20.00'),
(795, 21, 14, 178, 117, '40.00', '60.00', '40.00'),
(804, 21, 20, 164, 163, '0.00', '0.00', '0.00'),
(805, 21, 20, 166, 165, '0.00', '0.00', '0.00'),
(806, 21, 20, 168, 167, '20.00', '20.00', '20.00'),
(807, 21, 20, 170, 169, '20.00', '20.00', '20.00'),
(808, 21, 20, 172, 171, '80.00', '80.00', '80.00'),
(809, 21, 20, 174, 173, '40.00', '40.00', '40.00'),
(810, 21, 20, 176, 175, '60.00', '40.00', '40.00'),
(811, 21, 20, 178, 177, '60.00', '60.00', '60.00'),
(812, 20, 7, 180, 59, '10.00', '60.00', '10.00'),
(813, 20, 7, 181, 60, '20.00', '20.00', '20.00'),
(814, 20, 14, 180, 118, '20.00', '60.00', '20.00'),
(815, 20, 14, 181, 121, '20.00', '20.00', '20.00'),
(818, 20, 21, 177, 178, '60.00', '60.00', '60.00'),
(819, 20, 21, 180, 179, '60.00', '60.00', '60.00'),
(820, 21, 7, 179, 59, '10.00', '60.00', '10.00'),
(821, 21, 7, 182, 60, '20.00', '20.00', '20.00'),
(822, 21, 14, 179, 118, '20.00', '60.00', '20.00'),
(823, 21, 14, 182, 121, '20.00', '20.00', '20.00'),
(826, 21, 20, 179, 180, '60.00', '60.00', '60.00'),
(827, 21, 20, 182, 181, '20.00', '20.00', '20.00'),
(828, 22, 1, 183, 1, '0.00', '0.00', '0.00'),
(829, 22, 1, 184, 2, '0.00', '0.00', '0.00'),
(830, 22, 1, 185, 3, '5.00', '20.00', '5.00'),
(831, 22, 1, 186, 4, '10.00', '20.00', '10.00'),
(832, 22, 1, 187, 5, '40.00', '80.00', '40.00'),
(833, 22, 1, 188, 6, '25.00', '40.00', '25.00'),
(834, 22, 1, 189, 7, '5.00', '40.00', '5.00'),
(835, 22, 1, 190, 8, '20.00', '60.00', '20.00'),
(836, 22, 2, 183, 9, '0.00', '0.00', '0.00'),
(837, 22, 2, 184, 10, '0.00', '0.00', '0.00'),
(838, 22, 2, 185, 11, '5.00', '20.00', '5.00'),
(839, 22, 2, 186, 12, '5.00', '20.00', '5.00'),
(840, 22, 2, 187, 13, '40.00', '80.00', '40.00'),
(841, 22, 2, 188, 14, '25.00', '40.00', '25.00'),
(842, 22, 2, 189, 15, '20.00', '40.00', '20.00'),
(843, 22, 2, 190, 16, '5.00', '60.00', '5.00'),
(844, 22, 5, 183, 35, '0.00', '0.00', '0.00'),
(845, 22, 5, 184, 36, '0.00', '0.00', '0.00'),
(846, 22, 5, 185, 37, '20.00', '20.00', '20.00'),
(847, 22, 5, 186, 38, '0.00', '20.00', '0.00'),
(848, 22, 5, 187, 39, '0.00', '80.00', '0.00'),
(849, 22, 5, 188, 40, '30.00', '40.00', '30.00'),
(850, 22, 5, 189, 41, '10.00', '40.00', '10.00'),
(851, 22, 5, 190, 42, '5.00', '60.00', '5.00'),
(860, 22, 7, 183, 51, '0.00', '0.00', '0.00'),
(861, 22, 7, 184, 52, '0.00', '0.00', '0.00'),
(862, 22, 7, 185, 53, '20.00', '20.00', '20.00'),
(863, 22, 7, 186, 54, '20.00', '20.00', '20.00'),
(864, 22, 7, 187, 55, '40.00', '80.00', '40.00'),
(865, 22, 7, 188, 56, '5.00', '40.00', '5.00'),
(866, 22, 7, 189, 57, '5.00', '40.00', '5.00'),
(867, 22, 7, 190, 58, '5.00', '60.00', '5.00'),
(868, 22, 14, 183, 110, '0.00', '0.00', '0.00'),
(869, 22, 14, 184, 111, '0.00', '0.00', '0.00'),
(870, 22, 14, 185, 112, '20.00', '20.00', '20.00'),
(871, 22, 14, 186, 113, '20.00', '20.00', '20.00'),
(872, 22, 14, 187, 114, '0.00', '80.00', '0.00'),
(873, 22, 14, 188, 115, '10.00', '40.00', '10.00'),
(874, 22, 14, 189, 116, '5.00', '40.00', '5.00'),
(875, 22, 14, 190, 117, '5.00', '60.00', '5.00'),
(876, 22, 20, 183, 163, '0.00', '0.00', '0.00'),
(877, 22, 20, 184, 165, '0.00', '0.00', '0.00'),
(878, 22, 20, 185, 167, '20.00', '20.00', '20.00'),
(879, 22, 20, 186, 169, '20.00', '20.00', '20.00'),
(880, 22, 20, 187, 171, '0.00', '80.00', '0.00'),
(881, 22, 20, 188, 173, '10.00', '40.00', '10.00'),
(882, 22, 20, 189, 175, '5.00', '40.00', '5.00'),
(883, 22, 20, 190, 177, '5.00', '60.00', '5.00'),
(884, 22, 21, 183, 164, '0.00', '0.00', '0.00'),
(885, 22, 21, 184, 166, '0.00', '0.00', '0.00'),
(886, 22, 21, 185, 168, '20.00', '20.00', '20.00'),
(887, 22, 21, 186, 170, '20.00', '20.00', '20.00'),
(888, 22, 21, 187, 172, '0.00', '80.00', '0.00'),
(889, 22, 21, 188, 174, '10.00', '40.00', '10.00'),
(890, 22, 21, 189, 176, '5.00', '40.00', '5.00'),
(891, 22, 21, 190, 178, '5.00', '60.00', '5.00'),
(892, 22, 7, 191, 59, '5.00', '60.00', '5.00'),
(893, 22, 7, 192, 60, '10.00', '20.00', '10.00'),
(894, 22, 14, 191, 118, '5.00', '60.00', '5.00'),
(895, 22, 14, 192, 121, '10.00', '20.00', '10.00'),
(896, 22, 20, 191, 180, '5.00', '60.00', '5.00'),
(897, 22, 20, 192, 181, '10.00', '20.00', '10.00'),
(898, 22, 21, 191, 179, '5.00', '60.00', '5.00'),
(899, 22, 21, 192, 182, '10.00', '20.00', '10.00'),
(1064, 25, 1, 213, 1, '0.00', '0.00', '0.00'),
(1065, 25, 1, 214, 2, '0.00', '0.00', '0.00'),
(1066, 25, 1, 215, 3, '20.00', '20.00', '20.00'),
(1067, 25, 1, 216, 4, '10.00', '20.00', '10.00'),
(1068, 25, 1, 217, 5, '20.00', '80.00', '20.00'),
(1069, 25, 1, 218, 6, '5.00', '40.00', '5.00'),
(1070, 25, 1, 219, 7, '10.00', '40.00', '10.00'),
(1071, 25, 1, 220, 8, '5.00', '60.00', '5.00'),
(1072, 25, 2, 213, 9, '0.00', '0.00', '0.00'),
(1073, 25, 2, 214, 10, '0.00', '0.00', '0.00'),
(1074, 25, 2, 215, 11, '10.00', '20.00', '10.00'),
(1075, 25, 2, 216, 12, '5.00', '20.00', '5.00'),
(1076, 25, 2, 217, 13, '40.00', '80.00', '40.00'),
(1077, 25, 2, 218, 14, '5.00', '40.00', '5.00'),
(1078, 25, 2, 219, 15, '25.00', '40.00', '25.00'),
(1079, 25, 2, 220, 16, '5.00', '60.00', '5.00'),
(1080, 25, 5, 213, 35, '0.00', '0.00', '0.00'),
(1081, 25, 5, 214, 36, '0.00', '0.00', '0.00'),
(1082, 25, 5, 215, 37, '5.00', '20.00', '5.00'),
(1083, 25, 5, 216, 38, '0.00', '20.00', '0.00'),
(1084, 25, 5, 217, 39, '0.00', '80.00', '0.00'),
(1085, 25, 5, 218, 40, '20.00', '40.00', '20.00'),
(1086, 25, 5, 219, 41, '10.00', '40.00', '10.00'),
(1087, 25, 5, 220, 42, '10.00', '60.00', '10.00'),
(1088, 25, 7, 213, 51, '0.00', '0.00', '0.00'),
(1089, 25, 7, 214, 52, '0.00', '0.00', '0.00'),
(1090, 25, 7, 215, 53, '5.00', '20.00', '5.00'),
(1091, 25, 7, 216, 54, '20.00', '20.00', '20.00'),
(1092, 25, 7, 217, 55, '20.00', '80.00', '20.00'),
(1093, 25, 7, 218, 56, '5.00', '40.00', '5.00'),
(1094, 25, 7, 219, 57, '5.00', '40.00', '5.00'),
(1095, 25, 7, 220, 58, '5.00', '60.00', '5.00'),
(1096, 25, 14, 213, 110, '0.00', '0.00', '0.00'),
(1097, 25, 14, 214, 111, '0.00', '0.00', '0.00'),
(1098, 25, 14, 215, 112, '5.00', '20.00', '5.00'),
(1099, 25, 14, 216, 113, '20.00', '20.00', '20.00'),
(1100, 25, 14, 217, 114, '0.00', '80.00', '0.00'),
(1101, 25, 14, 218, 115, '20.00', '40.00', '20.00'),
(1102, 25, 14, 219, 116, '5.00', '40.00', '5.00'),
(1103, 25, 14, 220, 117, '5.00', '60.00', '5.00'),
(1104, 25, 20, 213, 163, '0.00', '0.00', '0.00'),
(1105, 25, 20, 214, 165, '0.00', '0.00', '0.00'),
(1106, 25, 20, 215, 167, '5.00', '20.00', '5.00'),
(1107, 25, 20, 216, 169, '20.00', '20.00', '20.00'),
(1108, 25, 20, 217, 171, '0.00', '80.00', '0.00'),
(1109, 25, 20, 218, 173, '25.00', '40.00', '25.00'),
(1110, 25, 20, 219, 175, '10.00', '40.00', '10.00'),
(1111, 25, 20, 220, 177, '5.00', '60.00', '5.00'),
(1112, 25, 21, 213, 164, '0.00', '0.00', '0.00'),
(1113, 25, 21, 214, 166, '0.00', '0.00', '0.00'),
(1114, 25, 21, 215, 168, '5.00', '20.00', '5.00'),
(1115, 25, 21, 216, 170, '20.00', '20.00', '20.00'),
(1116, 25, 21, 217, 172, '0.00', '80.00', '0.00'),
(1117, 25, 21, 218, 174, '25.00', '40.00', '25.00'),
(1118, 25, 21, 219, 176, '10.00', '40.00', '10.00'),
(1119, 25, 21, 220, 178, '5.00', '60.00', '5.00'),
(1120, 25, 22, 213, 183, '0.00', '0.00', '0.00'),
(1121, 25, 22, 214, 184, '0.00', '0.00', '0.00'),
(1122, 25, 22, 215, 185, '5.00', '20.00', '5.00'),
(1123, 25, 22, 216, 186, '20.00', '20.00', '20.00'),
(1124, 25, 22, 217, 187, '40.00', '80.00', '40.00'),
(1125, 25, 22, 218, 188, '5.00', '40.00', '5.00'),
(1126, 25, 22, 219, 189, '5.00', '40.00', '5.00'),
(1127, 25, 22, 220, 190, '5.00', '60.00', '5.00'),
(1128, 25, 7, 221, 59, '5.00', '60.00', '5.00'),
(1129, 25, 7, 222, 60, '10.00', '20.00', '10.00'),
(1130, 25, 14, 221, 118, '5.00', '60.00', '5.00'),
(1131, 25, 14, 222, 121, '10.00', '20.00', '10.00'),
(1132, 25, 20, 221, 180, '5.00', '60.00', '5.00'),
(1133, 25, 20, 222, 181, '10.00', '20.00', '10.00'),
(1134, 25, 21, 221, 179, '5.00', '60.00', '5.00'),
(1135, 25, 21, 222, 182, '10.00', '20.00', '10.00'),
(1136, 25, 22, 221, 191, '20.00', '60.00', '20.00'),
(1137, 25, 22, 222, 192, '20.00', '20.00', '20.00'),
(1138, 31, 1, 228, 1, '0.00', '0.00', '0.00'),
(1139, 31, 1, 229, 2, '0.00', '0.00', '0.00'),
(1140, 31, 1, 230, 3, '20.00', '20.00', '20.00'),
(1141, 31, 1, 231, 4, '10.00', '20.00', '10.00'),
(1142, 31, 1, 232, 5, '60.00', '80.00', '60.00'),
(1143, 31, 1, 233, 6, '25.00', '40.00', '25.00'),
(1144, 31, 1, 234, 7, '30.00', '40.00', '30.00'),
(1145, 31, 1, 235, 8, '20.00', '60.00', '20.00'),
(1146, 31, 2, 228, 9, '0.00', '0.00', '0.00'),
(1147, 31, 2, 229, 10, '0.00', '0.00', '0.00'),
(1148, 31, 2, 230, 11, '10.00', '20.00', '10.00'),
(1149, 31, 2, 231, 12, '5.00', '20.00', '5.00'),
(1150, 31, 2, 232, 13, '80.00', '80.00', '80.00'),
(1151, 31, 2, 233, 14, '15.00', '40.00', '15.00'),
(1152, 31, 2, 234, 15, '25.00', '40.00', '25.00'),
(1153, 31, 2, 235, 16, '5.00', '60.00', '5.00'),
(1154, 31, 5, 228, 35, '0.00', '0.00', '0.00'),
(1155, 31, 5, 229, 36, '0.00', '0.00', '0.00'),
(1156, 31, 5, 230, 37, '5.00', '20.00', '5.00'),
(1157, 31, 5, 231, 38, '0.00', '20.00', '0.00'),
(1158, 31, 5, 232, 39, '40.00', '80.00', '40.00'),
(1159, 31, 5, 233, 40, '30.00', '40.00', '30.00'),
(1160, 31, 5, 234, 41, '25.00', '40.00', '25.00'),
(1161, 31, 5, 235, 42, '5.00', '60.00', '5.00'),
(1162, 31, 7, 228, 51, '0.00', '0.00', '0.00'),
(1163, 31, 7, 229, 52, '0.00', '0.00', '0.00'),
(1164, 31, 7, 230, 53, '5.00', '20.00', '5.00'),
(1165, 31, 7, 231, 54, '20.00', '20.00', '20.00'),
(1166, 31, 7, 232, 55, '0.00', '80.00', '0.00'),
(1167, 31, 7, 233, 56, '20.00', '40.00', '20.00'),
(1168, 31, 7, 234, 57, '5.00', '40.00', '5.00'),
(1169, 31, 7, 235, 58, '5.00', '60.00', '5.00'),
(1170, 31, 14, 228, 110, '0.00', '0.00', '0.00'),
(1171, 31, 14, 229, 111, '0.00', '0.00', '0.00'),
(1172, 31, 14, 230, 112, '5.00', '20.00', '5.00'),
(1173, 31, 14, 231, 113, '20.00', '20.00', '20.00'),
(1174, 31, 14, 232, 114, '40.00', '80.00', '40.00'),
(1175, 31, 14, 233, 115, '20.00', '40.00', '20.00'),
(1176, 31, 14, 234, 116, '5.00', '40.00', '5.00'),
(1177, 31, 14, 235, 117, '5.00', '60.00', '5.00'),
(1178, 31, 20, 228, 163, '0.00', '0.00', '0.00'),
(1179, 31, 20, 229, 165, '0.00', '0.00', '0.00'),
(1180, 31, 20, 230, 167, '5.00', '20.00', '5.00'),
(1181, 31, 20, 231, 169, '20.00', '20.00', '20.00'),
(1182, 31, 20, 232, 171, '40.00', '80.00', '40.00'),
(1183, 31, 20, 233, 173, '40.00', '40.00', '40.00'),
(1184, 31, 20, 234, 175, '25.00', '40.00', '25.00'),
(1185, 31, 20, 235, 177, '5.00', '60.00', '5.00'),
(1186, 31, 21, 228, 164, '0.00', '0.00', '0.00'),
(1187, 31, 21, 229, 166, '0.00', '0.00', '0.00'),
(1188, 31, 21, 230, 168, '5.00', '20.00', '5.00'),
(1189, 31, 21, 231, 170, '20.00', '20.00', '20.00'),
(1190, 31, 21, 232, 172, '40.00', '80.00', '40.00'),
(1191, 31, 21, 233, 174, '40.00', '40.00', '40.00'),
(1192, 31, 21, 234, 176, '25.00', '40.00', '25.00'),
(1193, 31, 21, 235, 178, '5.00', '60.00', '5.00'),
(1194, 31, 22, 228, 183, '0.00', '0.00', '0.00'),
(1195, 31, 22, 229, 184, '0.00', '0.00', '0.00'),
(1196, 31, 22, 230, 185, '5.00', '20.00', '5.00'),
(1197, 31, 22, 231, 186, '20.00', '20.00', '20.00'),
(1198, 31, 22, 232, 187, '40.00', '80.00', '40.00'),
(1199, 31, 22, 233, 188, '20.00', '40.00', '20.00'),
(1200, 31, 22, 234, 189, '5.00', '40.00', '5.00'),
(1201, 31, 22, 235, 190, '20.00', '60.00', '20.00'),
(1202, 31, 25, 228, 213, '0.00', '0.00', '0.00'),
(1203, 31, 25, 229, 214, '0.00', '0.00', '0.00'),
(1204, 31, 25, 230, 215, '20.00', '20.00', '20.00'),
(1205, 31, 25, 231, 216, '20.00', '20.00', '20.00'),
(1206, 31, 25, 232, 217, '40.00', '80.00', '40.00'),
(1207, 31, 25, 233, 218, '25.00', '40.00', '25.00'),
(1208, 31, 25, 234, 219, '30.00', '40.00', '30.00'),
(1209, 31, 25, 235, 220, '5.00', '60.00', '5.00'),
(1210, 31, 29, 228, 226, '0.00', '0.00', '0.00'),
(1211, 31, 30, 228, 227, '0.00', '0.00', '0.00'),
(1212, 32, 1, 236, 1, '0.00', '0.00', '0.00'),
(1213, 32, 1, 237, 2, '0.00', '0.00', '0.00'),
(1214, 32, 1, 238, 3, '5.00', '20.00', '5.00'),
(1215, 32, 1, 239, 4, '10.00', '20.00', '10.00'),
(1216, 32, 1, 240, 5, '40.00', '80.00', '40.00'),
(1217, 32, 1, 241, 6, '10.00', '40.00', '10.00'),
(1218, 32, 1, 242, 7, '30.00', '40.00', '30.00'),
(1219, 32, 1, 243, 8, '30.00', '60.00', '30.00'),
(1220, 32, 2, 236, 9, '0.00', '0.00', '0.00'),
(1221, 32, 2, 237, 10, '0.00', '0.00', '0.00'),
(1222, 32, 2, 238, 11, '5.00', '20.00', '5.00'),
(1223, 32, 2, 239, 12, '20.00', '20.00', '20.00'),
(1224, 32, 2, 240, 13, '40.00', '80.00', '40.00'),
(1225, 32, 2, 241, 14, '20.00', '40.00', '20.00'),
(1226, 32, 2, 242, 15, '15.00', '40.00', '15.00'),
(1227, 32, 2, 243, 16, '45.00', '60.00', '45.00'),
(1228, 32, 5, 236, 35, '0.00', '0.00', '0.00'),
(1229, 32, 5, 237, 36, '0.00', '0.00', '0.00'),
(1230, 32, 5, 238, 37, '20.00', '20.00', '20.00'),
(1231, 32, 5, 239, 38, '5.00', '20.00', '5.00'),
(1232, 32, 5, 240, 39, '40.00', '80.00', '40.00'),
(1233, 32, 5, 241, 40, '5.00', '40.00', '5.00'),
(1234, 32, 5, 242, 41, '45.00', '40.00', '40.00'),
(1235, 32, 5, 243, 42, '45.00', '60.00', '45.00'),
(1236, 32, 7, 236, 51, '0.00', '0.00', '0.00'),
(1237, 32, 7, 237, 52, '0.00', '0.00', '0.00'),
(1238, 32, 7, 238, 53, '20.00', '20.00', '20.00'),
(1239, 32, 7, 239, 54, '5.00', '20.00', '5.00'),
(1240, 32, 7, 240, 55, '0.00', '80.00', '0.00'),
(1241, 32, 7, 241, 56, '5.00', '40.00', '5.00'),
(1242, 32, 7, 242, 57, '20.00', '40.00', '20.00'),
(1243, 32, 7, 243, 58, '20.00', '60.00', '20.00'),
(1244, 32, 14, 236, 110, '0.00', '0.00', '0.00'),
(1245, 32, 14, 237, 111, '0.00', '0.00', '0.00'),
(1246, 32, 14, 238, 112, '20.00', '20.00', '20.00'),
(1247, 32, 14, 239, 113, '5.00', '20.00', '5.00'),
(1248, 32, 14, 240, 114, '40.00', '80.00', '40.00'),
(1249, 32, 14, 241, 115, '5.00', '40.00', '5.00'),
(1250, 32, 14, 242, 116, '20.00', '40.00', '20.00'),
(1251, 32, 14, 243, 117, '40.00', '60.00', '40.00'),
(1252, 32, 20, 236, 163, '0.00', '0.00', '0.00'),
(1253, 32, 20, 237, 165, '0.00', '0.00', '0.00'),
(1254, 32, 20, 238, 167, '20.00', '20.00', '20.00'),
(1255, 32, 20, 239, 169, '5.00', '20.00', '5.00'),
(1256, 32, 20, 240, 171, '40.00', '80.00', '40.00'),
(1257, 32, 20, 241, 173, '5.00', '40.00', '5.00'),
(1258, 32, 20, 242, 175, '30.00', '40.00', '30.00'),
(1259, 32, 20, 243, 177, '60.00', '60.00', '60.00'),
(1260, 32, 21, 236, 164, '0.00', '0.00', '0.00'),
(1261, 32, 21, 237, 166, '0.00', '0.00', '0.00'),
(1262, 32, 21, 238, 168, '20.00', '20.00', '20.00'),
(1263, 32, 21, 239, 170, '5.00', '20.00', '5.00'),
(1264, 32, 21, 240, 172, '40.00', '80.00', '40.00'),
(1265, 32, 21, 241, 174, '5.00', '40.00', '5.00'),
(1266, 32, 21, 242, 176, '30.00', '40.00', '30.00'),
(1267, 32, 21, 243, 178, '60.00', '60.00', '60.00'),
(1268, 32, 22, 236, 183, '0.00', '0.00', '0.00'),
(1269, 32, 22, 237, 184, '0.00', '0.00', '0.00'),
(1270, 32, 22, 238, 185, '20.00', '20.00', '20.00'),
(1271, 32, 22, 239, 186, '5.00', '20.00', '5.00'),
(1272, 32, 22, 240, 187, '0.00', '80.00', '0.00'),
(1273, 32, 22, 241, 188, '5.00', '40.00', '5.00'),
(1274, 32, 22, 242, 189, '5.00', '40.00', '5.00'),
(1275, 32, 22, 243, 190, '5.00', '60.00', '5.00'),
(1276, 32, 25, 236, 213, '0.00', '0.00', '0.00'),
(1277, 32, 25, 237, 214, '0.00', '0.00', '0.00'),
(1278, 32, 25, 238, 215, '5.00', '20.00', '5.00'),
(1279, 32, 25, 239, 216, '5.00', '20.00', '5.00'),
(1280, 32, 25, 240, 217, '0.00', '80.00', '0.00'),
(1281, 32, 25, 241, 218, '25.00', '40.00', '25.00'),
(1282, 32, 25, 242, 219, '5.00', '40.00', '5.00'),
(1283, 32, 25, 243, 220, '5.00', '60.00', '5.00'),
(1284, 32, 29, 236, 226, '0.00', '0.00', '0.00'),
(1285, 32, 30, 236, 227, '0.00', '0.00', '0.00'),
(1286, 32, 31, 236, 228, '0.00', '0.00', '0.00'),
(1287, 32, 31, 237, 229, '0.00', '0.00', '0.00'),
(1288, 32, 31, 238, 230, '5.00', '20.00', '5.00'),
(1289, 32, 31, 239, 231, '5.00', '20.00', '5.00'),
(1290, 32, 31, 240, 232, '40.00', '80.00', '40.00'),
(1291, 32, 31, 241, 233, '10.00', '40.00', '10.00'),
(1292, 32, 31, 242, 234, '10.00', '40.00', '10.00'),
(1293, 32, 31, 243, 235, '5.00', '60.00', '5.00'),
(1294, 33, 1, 244, 1, '0.00', '0.00', '0.00'),
(1295, 33, 1, 245, 2, '0.00', '0.00', '0.00'),
(1296, 33, 1, 246, 3, '5.00', '20.00', '5.00'),
(1297, 33, 1, 247, 4, '20.00', '20.00', '20.00'),
(1298, 33, 1, 248, 5, '20.00', '80.00', '20.00'),
(1299, 33, 1, 249, 6, '40.00', '40.00', '40.00'),
(1300, 33, 1, 250, 7, '40.00', '40.00', '40.00'),
(1301, 33, 1, 251, 8, '15.00', '60.00', '15.00'),
(1302, 33, 2, 244, 9, '0.00', '0.00', '0.00'),
(1303, 33, 2, 245, 10, '0.00', '0.00', '0.00'),
(1304, 33, 2, 246, 11, '5.00', '20.00', '5.00'),
(1305, 33, 2, 247, 12, '10.00', '20.00', '10.00'),
(1306, 33, 2, 248, 13, '40.00', '80.00', '40.00'),
(1307, 33, 2, 249, 14, '10.00', '40.00', '10.00'),
(1308, 33, 2, 250, 15, '30.00', '40.00', '30.00'),
(1309, 33, 2, 251, 16, '30.00', '60.00', '30.00'),
(1310, 33, 5, 244, 35, '0.00', '0.00', '0.00'),
(1311, 33, 5, 245, 36, '0.00', '0.00', '0.00'),
(1312, 33, 5, 246, 37, '10.00', '20.00', '10.00'),
(1313, 33, 5, 247, 38, '0.00', '20.00', '0.00'),
(1314, 33, 5, 248, 39, '0.00', '80.00', '0.00'),
(1315, 33, 5, 249, 40, '25.00', '40.00', '25.00'),
(1316, 33, 5, 250, 41, '35.00', '40.00', '35.00'),
(1317, 33, 5, 251, 42, '60.00', '60.00', '60.00'),
(1318, 33, 7, 244, 51, '0.00', '0.00', '0.00'),
(1319, 33, 7, 245, 52, '0.00', '0.00', '0.00'),
(1320, 33, 7, 246, 53, '10.00', '20.00', '10.00'),
(1321, 33, 7, 247, 54, '10.00', '20.00', '10.00'),
(1322, 33, 7, 248, 55, '20.00', '80.00', '20.00'),
(1323, 33, 7, 249, 56, '20.00', '40.00', '20.00'),
(1324, 33, 7, 250, 57, '20.00', '40.00', '20.00'),
(1325, 33, 7, 251, 58, '5.00', '60.00', '5.00'),
(1326, 33, 14, 244, 110, '0.00', '0.00', '0.00'),
(1327, 33, 14, 245, 111, '0.00', '0.00', '0.00'),
(1328, 33, 14, 246, 112, '10.00', '20.00', '10.00'),
(1329, 33, 14, 247, 113, '10.00', '20.00', '10.00'),
(1330, 33, 14, 248, 114, '0.00', '80.00', '0.00'),
(1331, 33, 14, 249, 115, '5.00', '40.00', '5.00'),
(1332, 33, 14, 250, 116, '20.00', '40.00', '20.00'),
(1333, 33, 14, 251, 117, '25.00', '60.00', '25.00'),
(1334, 33, 20, 244, 163, '0.00', '0.00', '0.00'),
(1335, 33, 20, 245, 165, '0.00', '0.00', '0.00'),
(1336, 33, 20, 246, 167, '10.00', '20.00', '10.00'),
(1337, 33, 20, 247, 169, '10.00', '20.00', '10.00'),
(1338, 33, 20, 248, 171, '0.00', '80.00', '0.00'),
(1339, 33, 20, 249, 173, '25.00', '40.00', '25.00'),
(1340, 33, 20, 250, 175, '30.00', '40.00', '30.00'),
(1341, 33, 20, 251, 177, '45.00', '60.00', '45.00'),
(1342, 33, 21, 244, 164, '0.00', '0.00', '0.00'),
(1343, 33, 21, 245, 166, '0.00', '0.00', '0.00'),
(1344, 33, 21, 246, 168, '10.00', '20.00', '10.00'),
(1345, 33, 21, 247, 170, '10.00', '20.00', '10.00'),
(1346, 33, 21, 248, 172, '0.00', '80.00', '0.00'),
(1347, 33, 21, 249, 174, '25.00', '40.00', '25.00'),
(1348, 33, 21, 250, 176, '30.00', '40.00', '30.00'),
(1349, 33, 21, 251, 178, '45.00', '60.00', '45.00'),
(1350, 33, 22, 244, 183, '0.00', '0.00', '0.00'),
(1351, 33, 22, 245, 184, '0.00', '0.00', '0.00'),
(1352, 33, 22, 246, 185, '10.00', '20.00', '10.00'),
(1353, 33, 22, 247, 186, '10.00', '20.00', '10.00'),
(1354, 33, 22, 248, 187, '40.00', '80.00', '40.00'),
(1355, 33, 22, 249, 188, '25.00', '40.00', '25.00'),
(1356, 33, 22, 250, 189, '5.00', '40.00', '5.00'),
(1357, 33, 22, 251, 190, '5.00', '60.00', '5.00'),
(1358, 33, 25, 244, 213, '0.00', '0.00', '0.00'),
(1359, 33, 25, 245, 214, '0.00', '0.00', '0.00'),
(1360, 33, 25, 246, 215, '5.00', '20.00', '5.00'),
(1361, 33, 25, 247, 216, '10.00', '20.00', '10.00'),
(1362, 33, 25, 248, 217, '40.00', '80.00', '40.00'),
(1363, 33, 25, 249, 218, '5.00', '40.00', '5.00'),
(1364, 33, 25, 250, 219, '40.00', '40.00', '40.00'),
(1365, 33, 25, 251, 220, '5.00', '60.00', '5.00'),
(1366, 33, 29, 244, 226, '0.00', '0.00', '0.00'),
(1367, 33, 30, 244, 227, '0.00', '0.00', '0.00'),
(1368, 33, 31, 244, 228, '0.00', '0.00', '0.00'),
(1369, 33, 31, 245, 229, '0.00', '0.00', '0.00'),
(1370, 33, 31, 246, 230, '5.00', '20.00', '5.00'),
(1371, 33, 31, 247, 231, '10.00', '20.00', '10.00'),
(1372, 33, 31, 248, 232, '40.00', '80.00', '40.00'),
(1373, 33, 31, 249, 233, '25.00', '40.00', '25.00'),
(1374, 33, 31, 250, 234, '30.00', '40.00', '30.00'),
(1375, 33, 31, 251, 235, '5.00', '60.00', '5.00'),
(1376, 33, 32, 244, 236, '0.00', '0.00', '0.00'),
(1377, 33, 32, 245, 237, '0.00', '0.00', '0.00'),
(1378, 33, 32, 246, 238, '10.00', '20.00', '10.00'),
(1379, 33, 32, 247, 239, '10.00', '20.00', '10.00'),
(1380, 33, 32, 248, 240, '0.00', '80.00', '0.00'),
(1381, 33, 32, 249, 241, '10.00', '40.00', '10.00'),
(1382, 33, 32, 250, 242, '30.00', '40.00', '30.00'),
(1383, 33, 32, 251, 243, '45.00', '60.00', '45.00'),
(1384, 33, 7, 252, 59, '5.00', '60.00', '5.00'),
(1385, 33, 7, 253, 60, '20.00', '20.00', '20.00'),
(1386, 33, 14, 252, 118, '20.00', '60.00', '20.00'),
(1387, 33, 14, 253, 121, '20.00', '20.00', '20.00'),
(1388, 33, 20, 252, 180, '20.00', '60.00', '20.00'),
(1389, 33, 20, 253, 181, '20.00', '20.00', '20.00'),
(1390, 33, 21, 252, 179, '20.00', '60.00', '20.00'),
(1391, 33, 21, 253, 182, '20.00', '20.00', '20.00'),
(1392, 33, 22, 252, 191, '5.00', '60.00', '5.00'),
(1393, 33, 22, 253, 192, '10.00', '20.00', '10.00'),
(1394, 33, 25, 252, 221, '5.00', '60.00', '5.00'),
(1395, 33, 25, 253, 222, '10.00', '20.00', '10.00'),
(1396, 34, 1, 254, 1, '0.00', '0.00', '0.00'),
(1397, 34, 1, 255, 2, '0.00', '0.00', '0.00'),
(1398, 34, 1, 256, 3, '10.00', '20.00', '10.00'),
(1399, 34, 1, 257, 4, '10.00', '20.00', '10.00'),
(1400, 34, 1, 258, 5, '40.00', '80.00', '40.00'),
(1401, 34, 1, 259, 6, '10.00', '40.00', '10.00'),
(1402, 34, 1, 260, 7, '15.00', '40.00', '15.00'),
(1403, 34, 1, 261, 8, '30.00', '60.00', '30.00'),
(1404, 34, 2, 254, 9, '0.00', '0.00', '0.00'),
(1405, 34, 2, 255, 10, '0.00', '0.00', '0.00'),
(1406, 34, 2, 256, 11, '20.00', '20.00', '20.00'),
(1407, 34, 2, 257, 12, '5.00', '20.00', '5.00'),
(1408, 34, 2, 258, 13, '40.00', '80.00', '40.00'),
(1409, 34, 2, 259, 14, '30.00', '40.00', '30.00'),
(1410, 34, 2, 260, 15, '30.00', '40.00', '30.00'),
(1411, 34, 2, 261, 16, '45.00', '60.00', '45.00'),
(1412, 34, 5, 254, 35, '0.00', '0.00', '0.00'),
(1413, 34, 5, 255, 36, '0.00', '0.00', '0.00'),
(1414, 34, 5, 256, 37, '5.00', '20.00', '5.00'),
(1415, 34, 5, 257, 38, '0.00', '20.00', '0.00'),
(1416, 34, 5, 258, 39, '80.00', '80.00', '80.00'),
(1417, 34, 5, 259, 40, '10.00', '40.00', '10.00'),
(1418, 34, 5, 260, 41, '20.00', '40.00', '20.00'),
(1419, 34, 5, 261, 42, '45.00', '60.00', '45.00'),
(1420, 34, 7, 254, 51, '0.00', '0.00', '0.00'),
(1421, 34, 7, 255, 52, '0.00', '0.00', '0.00'),
(1422, 34, 7, 256, 53, '5.00', '20.00', '5.00'),
(1423, 34, 7, 257, 54, '20.00', '20.00', '20.00'),
(1424, 34, 7, 258, 55, '40.00', '80.00', '40.00'),
(1425, 34, 7, 259, 56, '5.00', '40.00', '5.00'),
(1426, 34, 7, 260, 57, '5.00', '40.00', '5.00'),
(1427, 34, 7, 261, 58, '20.00', '60.00', '20.00'),
(1428, 34, 14, 254, 110, '0.00', '0.00', '0.00'),
(1429, 34, 14, 255, 111, '0.00', '0.00', '0.00'),
(1430, 34, 14, 256, 112, '5.00', '20.00', '5.00'),
(1431, 34, 14, 257, 113, '20.00', '20.00', '20.00'),
(1432, 34, 14, 258, 114, '40.00', '80.00', '40.00'),
(1433, 34, 14, 259, 115, '10.00', '40.00', '10.00'),
(1434, 34, 14, 260, 116, '5.00', '40.00', '5.00'),
(1435, 34, 14, 261, 117, '40.00', '60.00', '40.00'),
(1436, 34, 20, 254, 163, '0.00', '0.00', '0.00'),
(1437, 34, 20, 255, 165, '0.00', '0.00', '0.00'),
(1438, 34, 20, 256, 167, '5.00', '20.00', '5.00'),
(1439, 34, 20, 257, 169, '20.00', '20.00', '20.00'),
(1440, 34, 20, 258, 171, '40.00', '80.00', '40.00'),
(1441, 34, 20, 259, 173, '10.00', '40.00', '10.00'),
(1442, 34, 20, 260, 175, '20.00', '40.00', '20.00'),
(1443, 34, 20, 261, 177, '60.00', '60.00', '60.00'),
(1444, 34, 21, 254, 164, '0.00', '0.00', '0.00'),
(1445, 34, 21, 255, 166, '0.00', '0.00', '0.00'),
(1446, 34, 21, 256, 168, '5.00', '20.00', '5.00'),
(1447, 34, 21, 257, 170, '20.00', '20.00', '20.00'),
(1448, 34, 21, 258, 172, '40.00', '80.00', '40.00'),
(1449, 34, 21, 259, 174, '10.00', '40.00', '10.00'),
(1450, 34, 21, 260, 176, '20.00', '40.00', '20.00'),
(1451, 34, 21, 261, 178, '60.00', '60.00', '60.00'),
(1452, 34, 22, 254, 183, '0.00', '0.00', '0.00'),
(1453, 34, 22, 255, 184, '0.00', '0.00', '0.00'),
(1454, 34, 22, 256, 185, '5.00', '20.00', '5.00'),
(1455, 34, 22, 257, 186, '20.00', '20.00', '20.00'),
(1456, 34, 22, 258, 187, '0.00', '80.00', '0.00'),
(1457, 34, 22, 259, 188, '25.00', '40.00', '25.00'),
(1458, 34, 22, 260, 189, '5.00', '40.00', '5.00'),
(1459, 34, 22, 261, 190, '5.00', '60.00', '5.00'),
(1460, 34, 25, 254, 213, '0.00', '0.00', '0.00'),
(1461, 34, 25, 255, 214, '0.00', '0.00', '0.00'),
(1462, 34, 25, 256, 215, '10.00', '20.00', '10.00'),
(1463, 34, 25, 257, 216, '20.00', '20.00', '20.00'),
(1464, 34, 25, 258, 217, '0.00', '80.00', '0.00'),
(1465, 34, 25, 259, 218, '5.00', '40.00', '5.00'),
(1466, 34, 25, 260, 219, '40.00', '40.00', '40.00'),
(1467, 34, 25, 261, 220, '5.00', '60.00', '5.00'),
(1468, 34, 29, 254, 226, '0.00', '0.00', '0.00'),
(1469, 34, 30, 254, 227, '0.00', '0.00', '0.00'),
(1470, 34, 31, 254, 228, '0.00', '0.00', '0.00'),
(1471, 34, 31, 255, 229, '0.00', '0.00', '0.00'),
(1472, 34, 31, 256, 230, '10.00', '20.00', '10.00'),
(1473, 34, 31, 257, 231, '20.00', '20.00', '20.00'),
(1474, 34, 31, 258, 232, '40.00', '80.00', '40.00'),
(1475, 34, 31, 259, 233, '15.00', '40.00', '15.00'),
(1476, 34, 31, 260, 234, '30.00', '40.00', '30.00'),
(1477, 34, 31, 261, 235, '5.00', '60.00', '5.00'),
(1478, 34, 32, 254, 236, '0.00', '0.00', '0.00'),
(1479, 34, 32, 255, 237, '0.00', '0.00', '0.00'),
(1480, 34, 32, 256, 238, '5.00', '20.00', '5.00'),
(1481, 34, 32, 257, 239, '5.00', '20.00', '5.00'),
(1482, 34, 32, 258, 240, '40.00', '80.00', '40.00'),
(1483, 34, 32, 259, 241, '5.00', '40.00', '5.00'),
(1484, 34, 32, 260, 242, '30.00', '40.00', '30.00'),
(1485, 34, 32, 261, 243, '60.00', '60.00', '60.00'),
(1486, 34, 33, 254, 244, '0.00', '0.00', '0.00'),
(1487, 34, 33, 255, 245, '0.00', '0.00', '0.00'),
(1488, 34, 33, 256, 246, '5.00', '20.00', '5.00'),
(1489, 34, 33, 257, 247, '10.00', '20.00', '10.00'),
(1490, 34, 33, 258, 248, '0.00', '80.00', '0.00'),
(1491, 34, 33, 259, 249, '10.00', '40.00', '10.00'),
(1492, 34, 33, 260, 250, '50.00', '40.00', '40.00'),
(1493, 34, 33, 261, 251, '45.00', '60.00', '45.00'),
(1494, 35, 1, 262, 1, '0.00', '0.00', '0.00'),
(1495, 35, 1, 263, 2, '0.00', '0.00', '0.00'),
(1496, 35, 1, 264, 3, '5.00', '20.00', '5.00'),
(1497, 35, 1, 265, 4, '20.00', '20.00', '20.00'),
(1498, 35, 1, 266, 5, '40.00', '80.00', '40.00'),
(1499, 35, 1, 267, 6, '20.00', '40.00', '20.00'),
(1500, 35, 1, 268, 7, '30.00', '40.00', '30.00'),
(1501, 35, 1, 269, 8, '10.00', '60.00', '10.00'),
(1502, 35, 2, 262, 9, '0.00', '0.00', '0.00'),
(1503, 35, 2, 263, 10, '0.00', '0.00', '0.00'),
(1504, 35, 2, 264, 11, '5.00', '20.00', '5.00'),
(1505, 35, 2, 265, 12, '10.00', '20.00', '10.00'),
(1506, 35, 2, 266, 13, '0.00', '80.00', '0.00'),
(1507, 35, 2, 267, 14, '5.00', '40.00', '5.00'),
(1508, 35, 2, 268, 15, '30.00', '40.00', '30.00'),
(1509, 35, 2, 269, 16, '5.00', '60.00', '5.00'),
(1510, 35, 5, 262, 35, '0.00', '0.00', '0.00'),
(1511, 35, 5, 263, 36, '0.00', '0.00', '0.00'),
(1512, 35, 5, 264, 37, '20.00', '20.00', '20.00'),
(1513, 35, 5, 265, 38, '0.00', '20.00', '0.00'),
(1514, 35, 5, 266, 39, '0.00', '80.00', '0.00'),
(1515, 35, 5, 267, 40, '10.00', '40.00', '10.00'),
(1516, 35, 5, 268, 41, '45.00', '40.00', '40.00'),
(1517, 35, 5, 269, 42, '45.00', '60.00', '45.00'),
(1518, 35, 7, 262, 51, '0.00', '0.00', '0.00'),
(1519, 35, 7, 263, 52, '0.00', '0.00', '0.00'),
(1520, 35, 7, 264, 53, '20.00', '20.00', '20.00'),
(1521, 35, 7, 265, 54, '10.00', '20.00', '10.00'),
(1522, 35, 7, 266, 55, '40.00', '80.00', '40.00'),
(1523, 35, 7, 267, 56, '20.00', '40.00', '20.00'),
(1524, 35, 7, 268, 57, '20.00', '40.00', '20.00'),
(1525, 35, 7, 269, 58, '5.00', '60.00', '5.00'),
(1526, 35, 14, 262, 110, '0.00', '0.00', '0.00'),
(1527, 35, 14, 263, 111, '0.00', '0.00', '0.00'),
(1528, 35, 14, 264, 112, '20.00', '20.00', '20.00'),
(1529, 35, 14, 265, 113, '10.00', '20.00', '10.00'),
(1530, 35, 14, 266, 114, '0.00', '80.00', '0.00'),
(1531, 35, 14, 267, 115, '5.00', '40.00', '5.00'),
(1532, 35, 14, 268, 116, '20.00', '40.00', '20.00'),
(1533, 35, 14, 269, 117, '5.00', '60.00', '5.00'),
(1534, 35, 20, 262, 163, '0.00', '0.00', '0.00'),
(1535, 35, 20, 263, 165, '0.00', '0.00', '0.00'),
(1536, 35, 20, 264, 167, '20.00', '20.00', '20.00'),
(1537, 35, 20, 265, 169, '10.00', '20.00', '10.00'),
(1538, 35, 20, 266, 171, '0.00', '80.00', '0.00'),
(1539, 35, 20, 267, 173, '20.00', '40.00', '20.00'),
(1540, 35, 20, 268, 175, '30.00', '40.00', '30.00'),
(1541, 35, 20, 269, 177, '20.00', '60.00', '20.00'),
(1542, 35, 21, 262, 164, '0.00', '0.00', '0.00'),
(1543, 35, 21, 263, 166, '0.00', '0.00', '0.00'),
(1544, 35, 21, 264, 168, '20.00', '20.00', '20.00'),
(1545, 35, 21, 265, 170, '10.00', '20.00', '10.00'),
(1546, 35, 21, 266, 172, '0.00', '80.00', '0.00'),
(1547, 35, 21, 267, 174, '20.00', '40.00', '20.00'),
(1548, 35, 21, 268, 176, '30.00', '40.00', '30.00'),
(1549, 35, 21, 269, 178, '20.00', '60.00', '20.00'),
(1550, 35, 22, 262, 183, '0.00', '0.00', '0.00'),
(1551, 35, 22, 263, 184, '0.00', '0.00', '0.00'),
(1552, 35, 22, 264, 185, '20.00', '20.00', '20.00'),
(1553, 35, 22, 265, 186, '10.00', '20.00', '10.00'),
(1554, 35, 22, 266, 187, '40.00', '80.00', '40.00'),
(1555, 35, 22, 267, 188, '5.00', '40.00', '5.00'),
(1556, 35, 22, 268, 189, '20.00', '40.00', '20.00'),
(1557, 35, 22, 269, 190, '5.00', '60.00', '5.00'),
(1558, 35, 25, 262, 213, '0.00', '0.00', '0.00'),
(1559, 35, 25, 263, 214, '0.00', '0.00', '0.00'),
(1560, 35, 25, 264, 215, '5.00', '20.00', '5.00'),
(1561, 35, 25, 265, 216, '10.00', '20.00', '10.00'),
(1562, 35, 25, 266, 217, '20.00', '80.00', '20.00'),
(1563, 35, 25, 267, 218, '5.00', '40.00', '5.00'),
(1564, 35, 25, 268, 219, '10.00', '40.00', '10.00'),
(1565, 35, 25, 269, 220, '10.00', '60.00', '10.00'),
(1566, 35, 29, 262, 226, '0.00', '0.00', '0.00'),
(1567, 35, 30, 262, 227, '0.00', '0.00', '0.00'),
(1568, 35, 31, 262, 228, '0.00', '0.00', '0.00'),
(1569, 35, 31, 263, 229, '0.00', '0.00', '0.00'),
(1570, 35, 31, 264, 230, '5.00', '20.00', '5.00'),
(1571, 35, 31, 265, 231, '10.00', '20.00', '10.00'),
(1572, 35, 31, 266, 232, '0.00', '80.00', '0.00'),
(1573, 35, 31, 267, 233, '20.00', '40.00', '20.00'),
(1574, 35, 31, 268, 234, '10.00', '40.00', '10.00'),
(1575, 35, 31, 269, 235, '5.00', '60.00', '5.00'),
(1576, 35, 32, 262, 236, '0.00', '0.00', '0.00'),
(1577, 35, 32, 263, 237, '0.00', '0.00', '0.00'),
(1578, 35, 32, 264, 238, '20.00', '20.00', '20.00'),
(1579, 35, 32, 265, 239, '10.00', '20.00', '10.00'),
(1580, 35, 32, 266, 240, '0.00', '80.00', '0.00'),
(1581, 35, 32, 267, 241, '5.00', '40.00', '5.00'),
(1582, 35, 32, 268, 242, '45.00', '40.00', '40.00'),
(1583, 35, 32, 269, 243, '25.00', '60.00', '25.00'),
(1584, 35, 33, 262, 244, '0.00', '0.00', '0.00'),
(1585, 35, 33, 263, 245, '0.00', '0.00', '0.00'),
(1586, 35, 33, 264, 246, '10.00', '20.00', '10.00'),
(1587, 35, 33, 265, 247, '20.00', '20.00', '20.00'),
(1588, 35, 33, 266, 248, '20.00', '80.00', '20.00'),
(1589, 35, 33, 267, 249, '20.00', '40.00', '20.00'),
(1590, 35, 33, 268, 250, '30.00', '40.00', '30.00'),
(1591, 35, 33, 269, 251, '45.00', '60.00', '45.00'),
(1592, 35, 34, 262, 254, '0.00', '0.00', '0.00'),
(1593, 35, 34, 263, 255, '0.00', '0.00', '0.00'),
(1594, 35, 34, 264, 256, '5.00', '20.00', '5.00'),
(1595, 35, 34, 265, 257, '10.00', '20.00', '10.00'),
(1596, 35, 34, 266, 258, '0.00', '80.00', '0.00'),
(1597, 35, 34, 267, 259, '5.00', '40.00', '5.00'),
(1598, 35, 34, 268, 260, '15.00', '40.00', '15.00'),
(1599, 35, 34, 269, 261, '20.00', '60.00', '20.00'),
(1600, 36, 1, 270, 1, '0.00', '0.00', '0.00'),
(1601, 36, 1, 271, 2, '0.00', '0.00', '0.00'),
(1602, 36, 1, 272, 3, '20.00', '20.00', '20.00'),
(1603, 36, 1, 273, 4, '10.00', '20.00', '10.00'),
(1604, 36, 1, 274, 5, '0.00', '80.00', '0.00'),
(1605, 36, 1, 275, 6, '25.00', '40.00', '25.00'),
(1606, 36, 1, 276, 7, '25.00', '40.00', '25.00'),
(1607, 36, 1, 277, 8, '50.00', '60.00', '50.00'),
(1608, 36, 2, 270, 9, '0.00', '0.00', '0.00'),
(1609, 36, 2, 271, 10, '0.00', '0.00', '0.00'),
(1610, 36, 2, 272, 11, '10.00', '20.00', '10.00'),
(1611, 36, 2, 273, 12, '5.00', '20.00', '5.00'),
(1612, 36, 2, 274, 13, '0.00', '80.00', '0.00'),
(1613, 36, 2, 275, 14, '10.00', '40.00', '10.00'),
(1614, 36, 2, 276, 15, '10.00', '40.00', '10.00'),
(1615, 36, 2, 277, 16, '30.00', '60.00', '30.00'),
(1616, 36, 5, 270, 35, '0.00', '0.00', '0.00'),
(1617, 36, 5, 271, 36, '0.00', '0.00', '0.00'),
(1618, 36, 5, 272, 37, '5.00', '20.00', '5.00'),
(1619, 36, 5, 273, 38, '0.00', '20.00', '0.00'),
(1620, 36, 5, 274, 39, '40.00', '80.00', '40.00'),
(1621, 36, 5, 275, 40, '30.00', '40.00', '30.00'),
(1622, 36, 5, 276, 41, '45.00', '40.00', '40.00'),
(1623, 36, 5, 277, 42, '30.00', '60.00', '30.00'),
(1624, 36, 7, 270, 51, '0.00', '0.00', '0.00'),
(1625, 36, 7, 271, 52, '0.00', '0.00', '0.00'),
(1626, 36, 7, 272, 53, '5.00', '20.00', '5.00'),
(1627, 36, 7, 273, 54, '20.00', '20.00', '20.00'),
(1628, 36, 7, 274, 55, '40.00', '80.00', '40.00'),
(1629, 36, 7, 275, 56, '20.00', '40.00', '20.00'),
(1630, 36, 7, 276, 57, '20.00', '40.00', '20.00'),
(1631, 36, 7, 277, 58, '20.00', '60.00', '20.00'),
(1632, 36, 14, 270, 110, '0.00', '0.00', '0.00'),
(1633, 36, 14, 271, 111, '0.00', '0.00', '0.00'),
(1634, 36, 14, 272, 112, '5.00', '20.00', '5.00'),
(1635, 36, 14, 273, 113, '20.00', '20.00', '20.00'),
(1636, 36, 14, 274, 114, '0.00', '80.00', '0.00'),
(1637, 36, 14, 275, 115, '20.00', '40.00', '20.00'),
(1638, 36, 14, 276, 116, '20.00', '40.00', '20.00'),
(1639, 36, 14, 277, 117, '25.00', '60.00', '25.00'),
(1640, 36, 20, 270, 163, '0.00', '0.00', '0.00'),
(1641, 36, 20, 271, 165, '0.00', '0.00', '0.00'),
(1642, 36, 20, 272, 167, '5.00', '20.00', '5.00'),
(1643, 36, 20, 273, 169, '20.00', '20.00', '20.00'),
(1644, 36, 20, 274, 171, '0.00', '80.00', '0.00'),
(1645, 36, 20, 275, 173, '40.00', '40.00', '40.00'),
(1646, 36, 20, 276, 175, '25.00', '40.00', '25.00'),
(1647, 36, 20, 277, 177, '45.00', '60.00', '45.00'),
(1648, 36, 21, 270, 164, '0.00', '0.00', '0.00'),
(1649, 36, 21, 271, 166, '0.00', '0.00', '0.00'),
(1650, 36, 21, 272, 168, '5.00', '20.00', '5.00'),
(1651, 36, 21, 273, 170, '20.00', '20.00', '20.00'),
(1652, 36, 21, 274, 172, '0.00', '80.00', '0.00'),
(1653, 36, 21, 275, 174, '40.00', '40.00', '40.00'),
(1654, 36, 21, 276, 176, '25.00', '40.00', '25.00'),
(1655, 36, 21, 277, 178, '45.00', '60.00', '45.00'),
(1656, 36, 22, 270, 183, '0.00', '0.00', '0.00'),
(1657, 36, 22, 271, 184, '0.00', '0.00', '0.00'),
(1658, 36, 22, 272, 185, '5.00', '20.00', '5.00'),
(1659, 36, 22, 273, 186, '20.00', '20.00', '20.00'),
(1660, 36, 22, 274, 187, '0.00', '80.00', '0.00'),
(1661, 36, 22, 275, 188, '10.00', '40.00', '10.00'),
(1662, 36, 22, 276, 189, '5.00', '40.00', '5.00'),
(1663, 36, 22, 277, 190, '20.00', '60.00', '20.00'),
(1664, 36, 25, 270, 213, '0.00', '0.00', '0.00'),
(1665, 36, 25, 271, 214, '0.00', '0.00', '0.00'),
(1666, 36, 25, 272, 215, '20.00', '20.00', '20.00'),
(1667, 36, 25, 273, 216, '20.00', '20.00', '20.00'),
(1668, 36, 25, 274, 217, '0.00', '80.00', '0.00'),
(1669, 36, 25, 275, 218, '25.00', '40.00', '25.00'),
(1670, 36, 25, 276, 219, '10.00', '40.00', '10.00'),
(1671, 36, 25, 277, 220, '5.00', '60.00', '5.00'),
(1672, 36, 29, 270, 226, '0.00', '0.00', '0.00'),
(1673, 36, 30, 270, 227, '0.00', '0.00', '0.00'),
(1674, 36, 31, 270, 228, '0.00', '0.00', '0.00'),
(1675, 36, 31, 271, 229, '0.00', '0.00', '0.00'),
(1676, 36, 31, 272, 230, '20.00', '20.00', '20.00'),
(1677, 36, 31, 273, 231, '20.00', '20.00', '20.00'),
(1678, 36, 31, 274, 232, '0.00', '80.00', '0.00'),
(1679, 36, 31, 275, 233, '40.00', '40.00', '40.00'),
(1680, 36, 31, 276, 234, '10.00', '40.00', '10.00'),
(1681, 36, 31, 277, 235, '20.00', '60.00', '20.00'),
(1682, 36, 32, 270, 236, '0.00', '0.00', '0.00'),
(1683, 36, 32, 271, 237, '0.00', '0.00', '0.00'),
(1684, 36, 32, 272, 238, '5.00', '20.00', '5.00'),
(1685, 36, 32, 273, 239, '5.00', '20.00', '5.00'),
(1686, 36, 32, 274, 240, '0.00', '80.00', '0.00'),
(1687, 36, 32, 275, 241, '5.00', '40.00', '5.00'),
(1688, 36, 32, 276, 242, '45.00', '40.00', '40.00'),
(1689, 36, 32, 277, 243, '45.00', '60.00', '45.00'),
(1690, 36, 33, 270, 244, '0.00', '0.00', '0.00'),
(1691, 36, 33, 271, 245, '0.00', '0.00', '0.00'),
(1692, 36, 33, 272, 246, '5.00', '20.00', '5.00'),
(1693, 36, 33, 273, 247, '10.00', '20.00', '10.00'),
(1694, 36, 33, 274, 248, '0.00', '80.00', '0.00'),
(1695, 36, 33, 275, 249, '25.00', '40.00', '25.00'),
(1696, 36, 33, 276, 250, '25.00', '40.00', '25.00'),
(1697, 36, 33, 277, 251, '30.00', '60.00', '30.00'),
(1698, 36, 34, 270, 254, '0.00', '0.00', '0.00'),
(1699, 36, 34, 271, 255, '0.00', '0.00', '0.00'),
(1700, 36, 34, 272, 256, '10.00', '20.00', '10.00'),
(1701, 36, 34, 273, 257, '20.00', '20.00', '20.00'),
(1702, 36, 34, 274, 258, '40.00', '80.00', '40.00'),
(1703, 36, 34, 275, 259, '10.00', '40.00', '10.00'),
(1704, 36, 34, 276, 260, '10.00', '40.00', '10.00'),
(1705, 36, 34, 277, 261, '45.00', '60.00', '45.00'),
(1706, 36, 35, 270, 262, '0.00', '0.00', '0.00'),
(1707, 36, 35, 271, 263, '0.00', '0.00', '0.00'),
(1708, 36, 35, 272, 264, '5.00', '20.00', '5.00'),
(1709, 36, 35, 273, 265, '10.00', '20.00', '10.00'),
(1710, 36, 35, 274, 266, '0.00', '80.00', '0.00'),
(1711, 36, 35, 275, 267, '20.00', '40.00', '20.00'),
(1712, 36, 35, 276, 268, '50.00', '40.00', '40.00'),
(1713, 36, 35, 277, 269, '25.00', '60.00', '25.00'),
(1714, 37, 1, 278, 1, '0.00', '0.00', '0.00'),
(1715, 37, 1, 279, 2, '0.00', '0.00', '0.00'),
(1716, 37, 1, 280, 3, '20.00', '20.00', '20.00'),
(1717, 37, 1, 281, 4, '0.00', '20.00', '0.00'),
(1718, 37, 1, 282, 5, '0.00', '80.00', '0.00'),
(1719, 37, 1, 283, 6, '40.00', '40.00', '40.00'),
(1720, 37, 1, 284, 7, '25.00', '40.00', '25.00'),
(1721, 37, 1, 285, 8, '5.00', '60.00', '5.00'),
(1722, 37, 2, 278, 9, '0.00', '0.00', '0.00'),
(1723, 37, 2, 279, 10, '0.00', '0.00', '0.00'),
(1724, 37, 2, 280, 11, '10.00', '20.00', '10.00'),
(1725, 37, 2, 281, 12, '5.00', '20.00', '5.00'),
(1726, 37, 2, 282, 13, '0.00', '80.00', '0.00'),
(1727, 37, 2, 283, 14, '10.00', '40.00', '10.00'),
(1728, 37, 2, 284, 15, '15.00', '40.00', '15.00'),
(1729, 37, 2, 285, 16, '20.00', '60.00', '20.00'),
(1730, 37, 5, 278, 35, '0.00', '0.00', '0.00'),
(1731, 37, 5, 279, 36, '0.00', '0.00', '0.00'),
(1732, 37, 5, 280, 37, '5.00', '20.00', '5.00'),
(1733, 37, 5, 281, 38, '20.00', '20.00', '20.00'),
(1734, 37, 5, 282, 39, '40.00', '80.00', '40.00'),
(1735, 37, 5, 283, 40, '25.00', '40.00', '25.00'),
(1736, 37, 5, 284, 41, '45.00', '40.00', '40.00'),
(1737, 37, 5, 285, 42, '20.00', '60.00', '20.00'),
(1738, 37, 7, 278, 51, '0.00', '0.00', '0.00'),
(1739, 37, 7, 279, 52, '0.00', '0.00', '0.00'),
(1740, 37, 7, 280, 53, '5.00', '20.00', '5.00'),
(1741, 37, 7, 281, 54, '0.00', '20.00', '0.00'),
(1742, 37, 7, 282, 55, '40.00', '80.00', '40.00'),
(1743, 37, 7, 283, 56, '20.00', '40.00', '20.00'),
(1744, 37, 7, 284, 57, '20.00', '40.00', '20.00'),
(1745, 37, 7, 285, 58, '5.00', '60.00', '5.00'),
(1746, 37, 14, 278, 110, '0.00', '0.00', '0.00'),
(1747, 37, 14, 279, 111, '0.00', '0.00', '0.00'),
(1748, 37, 14, 280, 112, '5.00', '20.00', '5.00'),
(1749, 37, 14, 281, 113, '0.00', '20.00', '0.00'),
(1750, 37, 14, 282, 114, '0.00', '80.00', '0.00'),
(1751, 37, 14, 283, 115, '5.00', '40.00', '5.00'),
(1752, 37, 14, 284, 116, '20.00', '40.00', '20.00'),
(1753, 37, 14, 285, 117, '20.00', '60.00', '20.00'),
(1754, 37, 20, 278, 163, '0.00', '0.00', '0.00'),
(1755, 37, 20, 279, 165, '0.00', '0.00', '0.00'),
(1756, 37, 20, 280, 167, '5.00', '20.00', '5.00'),
(1757, 37, 20, 281, 169, '0.00', '20.00', '0.00'),
(1758, 37, 20, 282, 171, '0.00', '80.00', '0.00'),
(1759, 37, 20, 283, 173, '25.00', '40.00', '25.00'),
(1760, 37, 20, 284, 175, '25.00', '40.00', '25.00'),
(1761, 37, 20, 285, 177, '20.00', '60.00', '20.00'),
(1762, 37, 21, 278, 164, '0.00', '0.00', '0.00'),
(1763, 37, 21, 279, 166, '0.00', '0.00', '0.00'),
(1764, 37, 21, 280, 168, '5.00', '20.00', '5.00'),
(1765, 37, 21, 281, 170, '0.00', '20.00', '0.00'),
(1766, 37, 21, 282, 172, '0.00', '80.00', '0.00'),
(1767, 37, 21, 283, 174, '25.00', '40.00', '25.00'),
(1768, 37, 21, 284, 176, '25.00', '40.00', '25.00'),
(1769, 37, 21, 285, 178, '20.00', '60.00', '20.00'),
(1770, 37, 22, 278, 183, '0.00', '0.00', '0.00'),
(1771, 37, 22, 279, 184, '0.00', '0.00', '0.00'),
(1772, 37, 22, 280, 185, '5.00', '20.00', '5.00'),
(1773, 37, 22, 281, 186, '0.00', '20.00', '0.00'),
(1774, 37, 22, 282, 187, '0.00', '80.00', '0.00'),
(1775, 37, 22, 283, 188, '25.00', '40.00', '25.00'),
(1776, 37, 22, 284, 189, '10.00', '40.00', '10.00'),
(1777, 37, 22, 285, 190, '5.00', '60.00', '5.00'),
(1778, 37, 25, 278, 213, '0.00', '0.00', '0.00'),
(1779, 37, 25, 279, 214, '0.00', '0.00', '0.00'),
(1780, 37, 25, 280, 215, '20.00', '20.00', '20.00'),
(1781, 37, 25, 281, 216, '0.00', '20.00', '0.00'),
(1782, 37, 25, 282, 217, '0.00', '80.00', '0.00'),
(1783, 37, 25, 283, 218, '10.00', '40.00', '10.00'),
(1784, 37, 25, 284, 219, '10.00', '40.00', '10.00'),
(1785, 37, 25, 285, 220, '5.00', '60.00', '5.00'),
(1786, 37, 29, 278, 226, '0.00', '0.00', '0.00'),
(1787, 37, 30, 278, 227, '0.00', '0.00', '0.00'),
(1788, 37, 31, 278, 228, '0.00', '0.00', '0.00'),
(1789, 37, 31, 279, 229, '0.00', '0.00', '0.00'),
(1790, 37, 31, 280, 230, '20.00', '20.00', '20.00'),
(1791, 37, 31, 281, 231, '0.00', '20.00', '0.00'),
(1792, 37, 31, 282, 232, '0.00', '80.00', '0.00'),
(1793, 37, 31, 283, 233, '25.00', '40.00', '25.00'),
(1794, 37, 31, 284, 234, '10.00', '40.00', '10.00'),
(1795, 37, 31, 285, 235, '5.00', '60.00', '5.00'),
(1796, 37, 32, 278, 236, '0.00', '0.00', '0.00'),
(1797, 37, 32, 279, 237, '0.00', '0.00', '0.00'),
(1798, 37, 32, 280, 238, '5.00', '20.00', '5.00'),
(1799, 37, 32, 281, 239, '5.00', '20.00', '5.00'),
(1800, 37, 32, 282, 240, '0.00', '80.00', '0.00'),
(1801, 37, 32, 283, 241, '5.00', '40.00', '5.00'),
(1802, 37, 32, 284, 242, '50.00', '40.00', '40.00'),
(1803, 37, 32, 285, 243, '20.00', '60.00', '20.00'),
(1804, 37, 33, 278, 244, '0.00', '0.00', '0.00'),
(1805, 37, 33, 279, 245, '0.00', '0.00', '0.00'),
(1806, 37, 33, 280, 246, '5.00', '20.00', '5.00'),
(1807, 37, 33, 281, 247, '0.00', '20.00', '0.00'),
(1808, 37, 33, 282, 248, '0.00', '80.00', '0.00'),
(1809, 37, 33, 283, 249, '40.00', '40.00', '40.00'),
(1810, 37, 33, 284, 250, '25.00', '40.00', '25.00'),
(1811, 37, 33, 285, 251, '20.00', '60.00', '20.00'),
(1812, 37, 34, 278, 254, '0.00', '0.00', '0.00'),
(1813, 37, 34, 279, 255, '0.00', '0.00', '0.00'),
(1814, 37, 34, 280, 256, '10.00', '20.00', '10.00'),
(1815, 37, 34, 281, 257, '0.00', '20.00', '0.00'),
(1816, 37, 34, 282, 258, '40.00', '80.00', '40.00'),
(1817, 37, 34, 283, 259, '10.00', '40.00', '10.00'),
(1818, 37, 34, 284, 260, '15.00', '40.00', '15.00'),
(1819, 37, 34, 285, 261, '20.00', '60.00', '20.00'),
(1820, 37, 35, 278, 262, '0.00', '0.00', '0.00'),
(1821, 37, 35, 279, 263, '0.00', '0.00', '0.00'),
(1822, 37, 35, 280, 264, '5.00', '20.00', '5.00'),
(1823, 37, 35, 281, 265, '0.00', '20.00', '0.00'),
(1824, 37, 35, 282, 266, '0.00', '80.00', '0.00'),
(1825, 37, 35, 283, 267, '20.00', '40.00', '20.00'),
(1826, 37, 35, 284, 268, '45.00', '40.00', '40.00'),
(1827, 37, 35, 285, 269, '5.00', '60.00', '5.00'),
(1828, 37, 36, 278, 270, '0.00', '0.00', '0.00'),
(1829, 37, 36, 279, 271, '0.00', '0.00', '0.00'),
(1830, 37, 36, 280, 272, '20.00', '20.00', '20.00'),
(1831, 37, 36, 281, 273, '0.00', '20.00', '0.00'),
(1832, 37, 36, 282, 274, '40.00', '80.00', '40.00'),
(1833, 37, 36, 283, 275, '25.00', '40.00', '25.00'),
(1834, 37, 36, 284, 276, '40.00', '40.00', '40.00'),
(1835, 37, 36, 285, 277, '5.00', '60.00', '5.00'),
(1836, 37, 7, 286, 59, '20.00', '60.00', '20.00'),
(1837, 37, 7, 287, 60, '10.00', '20.00', '10.00'),
(1838, 37, 14, 286, 118, '5.00', '60.00', '5.00'),
(1839, 37, 14, 287, 121, '10.00', '20.00', '10.00'),
(1840, 37, 20, 286, 180, '10.00', '60.00', '10.00');
INSERT INTO `test_ques_comparisons` (`id`, `test_id_1`, `test_id_2`, `test_que_1`, `test_que_2`, `obtained_scores`, `max_allowed_scores`, `final_scores`) VALUES
(1841, 37, 20, 287, 181, '10.00', '20.00', '10.00'),
(1842, 37, 21, 286, 179, '10.00', '60.00', '10.00'),
(1843, 37, 21, 287, 182, '10.00', '20.00', '10.00'),
(1844, 37, 22, 286, 191, '5.00', '60.00', '5.00'),
(1845, 37, 22, 287, 192, '20.00', '20.00', '20.00'),
(1846, 37, 25, 286, 221, '5.00', '60.00', '5.00'),
(1847, 37, 25, 287, 222, '20.00', '20.00', '20.00'),
(1848, 37, 33, 286, 252, '5.00', '60.00', '5.00'),
(1849, 37, 33, 287, 253, '10.00', '20.00', '10.00');

-- --------------------------------------------------------

--
-- Table structure for table `test_scores`
--

CREATE TABLE `test_scores` (
  `id` int(11) NOT NULL,
  `test_id_1` int(11) NOT NULL,
  `test_id_2` int(11) NOT NULL,
  `scores` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `test_scores`
--

INSERT INTO `test_scores` (`id`, `test_id_1`, `test_id_2`, `scores`) VALUES
(1, 2, 1, '165.00'),
(7, 5, 1, '125.00'),
(8, 5, 2, '125.00'),
(13, 7, 1, '115.00'),
(14, 7, 2, '40.00'),
(15, 7, 5, '95.00'),
(44, 14, 1, '105.00'),
(45, 14, 2, '110.00'),
(46, 14, 5, '125.00'),
(48, 14, 7, '110.00'),
(81, 20, 1, '150.00'),
(82, 20, 2, '135.00'),
(83, 20, 5, '175.00'),
(84, 21, 1, '150.00'),
(86, 21, 2, '135.00'),
(87, 20, 7, '130.00'),
(88, 21, 5, '175.00'),
(89, 20, 14, '200.00'),
(92, 21, 7, '130.00'),
(93, 20, 21, '320.00'),
(94, 21, 14, '200.00'),
(96, 21, 20, '340.00'),
(97, 22, 1, '105.00'),
(98, 22, 2, '100.00'),
(99, 22, 5, '65.00'),
(101, 22, 7, '110.00'),
(102, 22, 14, '75.00'),
(103, 22, 20, '75.00'),
(104, 22, 21, '75.00'),
(123, 25, 1, '70.00'),
(124, 25, 2, '90.00'),
(125, 25, 5, '45.00'),
(126, 25, 7, '75.00'),
(127, 25, 14, '70.00'),
(128, 25, 20, '80.00'),
(129, 25, 21, '80.00'),
(130, 25, 22, '120.00'),
(131, 31, 1, '165.00'),
(132, 31, 2, '140.00'),
(133, 31, 5, '105.00'),
(134, 31, 7, '55.00'),
(135, 31, 14, '95.00'),
(136, 31, 20, '135.00'),
(137, 31, 21, '135.00'),
(138, 31, 22, '110.00'),
(139, 31, 25, '140.00'),
(140, 31, 29, '0.00'),
(141, 31, 30, '0.00'),
(142, 32, 1, '125.00'),
(143, 32, 2, '145.00'),
(144, 32, 5, '155.00'),
(145, 32, 7, '70.00'),
(146, 32, 14, '130.00'),
(147, 32, 20, '160.00'),
(148, 32, 21, '160.00'),
(149, 32, 22, '40.00'),
(150, 32, 25, '45.00'),
(151, 32, 29, '0.00'),
(152, 32, 30, '0.00'),
(153, 32, 31, '75.00'),
(154, 33, 1, '140.00'),
(155, 33, 2, '125.00'),
(156, 33, 5, '130.00'),
(157, 33, 7, '110.00'),
(158, 33, 14, '110.00'),
(159, 33, 20, '160.00'),
(160, 33, 21, '160.00'),
(161, 33, 22, '110.00'),
(162, 33, 25, '120.00'),
(163, 33, 29, '0.00'),
(164, 33, 30, '0.00'),
(165, 33, 31, '115.00'),
(166, 33, 32, '105.00'),
(167, 34, 1, '115.00'),
(168, 34, 2, '170.00'),
(169, 34, 5, '160.00'),
(170, 34, 7, '95.00'),
(171, 34, 14, '120.00'),
(172, 34, 20, '155.00'),
(173, 34, 21, '155.00'),
(174, 34, 22, '60.00'),
(175, 34, 25, '80.00'),
(176, 34, 29, '0.00'),
(177, 34, 30, '0.00'),
(178, 34, 31, '120.00'),
(179, 34, 32, '145.00'),
(180, 34, 33, '110.00'),
(181, 35, 1, '125.00'),
(182, 35, 2, '55.00'),
(183, 35, 5, '115.00'),
(184, 35, 7, '115.00'),
(185, 35, 14, '60.00'),
(186, 35, 20, '100.00'),
(187, 35, 21, '100.00'),
(188, 35, 22, '100.00'),
(189, 35, 25, '60.00'),
(190, 35, 29, '0.00'),
(191, 35, 30, '0.00'),
(192, 35, 31, '50.00'),
(193, 35, 32, '100.00'),
(194, 35, 33, '145.00'),
(195, 35, 34, '55.00'),
(196, 36, 1, '130.00'),
(197, 36, 2, '65.00'),
(198, 36, 5, '145.00'),
(199, 36, 7, '125.00'),
(200, 36, 14, '90.00'),
(201, 36, 20, '135.00'),
(202, 36, 21, '135.00'),
(203, 36, 22, '60.00'),
(204, 36, 25, '80.00'),
(205, 36, 29, '0.00'),
(206, 36, 30, '0.00'),
(207, 36, 31, '110.00'),
(208, 36, 32, '100.00'),
(209, 36, 33, '95.00'),
(210, 36, 34, '135.00'),
(211, 36, 35, '100.00'),
(212, 37, 1, '90.00'),
(213, 37, 2, '60.00'),
(214, 37, 5, '150.00'),
(215, 37, 7, '120.00'),
(216, 37, 14, '65.00'),
(217, 37, 20, '95.00'),
(218, 37, 21, '95.00'),
(219, 37, 22, '70.00'),
(220, 37, 25, '70.00'),
(221, 37, 29, '0.00'),
(222, 37, 30, '0.00'),
(223, 37, 31, '60.00'),
(224, 37, 32, '75.00'),
(225, 37, 33, '105.00'),
(226, 37, 34, '95.00'),
(227, 37, 35, '70.00'),
(228, 37, 36, '130.00');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(10) UNSIGNED NOT NULL,
  `locale` enum('en','de') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'en' COMMENT 'language en=english, de=germany',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `username` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL COMMENT '1=> male, 2=> female, 3=transgender',
  `login_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1=> manual,2=> facebook, 3=> google, 4=> instagram',
  `social_id` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `verification_code` int(11) DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'google location',
  `latitude` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `longitude` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone_code` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `love_push_intention` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '1= relationship, 2= friendship, 3= daily activities, 4= no expactations',
  `relationship` tinyint(5) DEFAULT NULL COMMENT '1= single, 2= in relationship, 3= married, 4= recently separated, 5= pen relationship',
  `size` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `physique` tinyint(5) DEFAULT NULL COMMENT '1= plump, 2= thin, 3= athletic, 4= muscular, 5= normal',
  `eyecolor` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `haircolor` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `education` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'favorite_travel_destination consider',
  `job` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hobbies` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `religion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `aboutme` text COLLATE utf8mb4_unicode_ci,
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0=> de-active, 1=> active',
  `is_restrict_fb_friends` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1=> not restrict, 0=> restrict',
  `sexual_orientation` tinyint(4) DEFAULT NULL COMMENT '1=> hetrosexual, 2=> bisexual, 3=> homesexual',
  `partner_preference` tinyint(4) NOT NULL DEFAULT '3' COMMENT '(NotUsed)1=> male, 2=> female, 3=> anyone',
  `is_profile_complete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0=> not, 1=> yes',
  `device_token` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `device_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1=> android, 2=> ios',
  `profile_image` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `like_user_ids` text COLLATE utf8mb4_unicode_ci COMMENT 'id of like and dislike user_id from likes table',
  `connect_user_ids` text COLLATE utf8mb4_unicode_ci COMMENT 'id of accept connect to_user_id from connect_requests table',
  `chat_user_ids` text COLLATE utf8mb4_unicode_ci COMMENT 'id of accept chat to_user_id from chat_requests table',
  `like_request_ids` text COLLATE utf8mb4_unicode_ci,
  `connect_request_ids` text COLLATE utf8mb4_unicode_ci,
  `chat_request_ids` text COLLATE utf8mb4_unicode_ci,
  `language` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'manually enter',
  `role` tinyint(4) NOT NULL DEFAULT '2' COMMENT '1=> admin, 2=> normal users',
  `country` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Not Used',
  `city` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Not Used',
  `subscription_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '(Not Used)1=>free, 2=>premium, 3=>vip',
  `subscription_expire_date` date DEFAULT NULL,
  `remember_token` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_phone_verify` tinyint(4) NOT NULL DEFAULT '0' COMMENT '(Not Used)0=> not, 1=> yes ',
  `employer` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Not Used',
  `myReferalCode` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `referalBy` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reset_link` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hide_from_search` enum('0','1') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '0 - diable, 1 - enable',
  `receive_like_request` enum('0','1') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '0 - diable, 1 - enable',
  `receive_chat_request` enum('0','1') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '0 - diable, 1 - enable',
  `receive_notification` enum('0','1') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '0 - disable, 1 - enable',
  `isBlocked` enum('0','1') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '0 - unblock, 1 - block',
  `isDeleted` enum('0','1') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '0 - undelete, 1 - delete',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `register_from` enum('','web') COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'web= from website',
  `quickblox_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'quickblox userId'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `locale`, `name`, `username`, `email`, `gender`, `login_type`, `social_id`, `age`, `email_verified_at`, `verification_code`, `address`, `latitude`, `longitude`, `phone_code`, `phone`, `password`, `love_push_intention`, `relationship`, `size`, `physique`, `eyecolor`, `haircolor`, `education`, `job`, `hobbies`, `religion`, `aboutme`, `status`, `is_restrict_fb_friends`, `sexual_orientation`, `partner_preference`, `is_profile_complete`, `device_token`, `device_type`, `profile_image`, `like_user_ids`, `connect_user_ids`, `chat_user_ids`, `like_request_ids`, `connect_request_ids`, `chat_request_ids`, `language`, `role`, `country`, `city`, `subscription_type`, `subscription_expire_date`, `remember_token`, `is_phone_verify`, `employer`, `myReferalCode`, `referalBy`, `reset_link`, `hide_from_search`, `receive_like_request`, `receive_chat_request`, `receive_notification`, `isBlocked`, `isDeleted`, `created_at`, `updated_at`, `register_from`, `quickblox_id`) VALUES
(1344, 'en', 'Clark Joy', 'clark', 'clark@mailinator.com', 1, 1, NULL, 18, '2021-11-01 05:59:33', NULL, '33, Industrial Area, Sector 74, Sahibzada Ajit Singh Nagar, Punjab 160055, India', '30.7065933', '76.6853477', '+1', '', '$2y$10$CwEgcZjCiBNfJP8omCRiW.7k7WRluSEb.oBk8lQfZxHGpejTEJEIK', 'Relationship, Friendship, Daily Activities', 1, NULL, NULL, NULL, NULL, 'Sample Destination', 'Sample Job', 'Sample Hobbies', NULL, 'Sample About Me', 1, 1, 1, 3, 1, NULL, 1, '1635766288.', '1345', NULL, NULL, '1345', '1346,1347', NULL, 'English', 2, NULL, NULL, 1, NULL, NULL, 0, NULL, 'YRV2ZS', '', NULL, '0', '1', '1', '1', '0', '0', '2021-11-01 05:59:20', '2021-11-06 11:52:00', NULL, '131599077'),
(1345, 'en', 'Smith Joy', 'Smith', 'smith@mailinator.com', 1, 1, NULL, 18, '2021-11-01 06:06:42', NULL, '1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA', '37.421983', '-122.0842119', '+1', '', '$2y$10$EceqHjbyPtCLrcKh6FnVCeq4DYr20JpUJtKuR48OJO3micU5d1Ir6', 'Relationship, Friendship, Daily Activities', 1, NULL, NULL, NULL, NULL, 'Sample', 'Sample', 'Sample', NULL, 'Sample', 1, 1, 1, 3, 1, NULL, 1, '1635766652.', '1344', NULL, NULL, '1344', '1346,1347', NULL, 'Sampleq', 2, NULL, NULL, 1, NULL, NULL, 0, NULL, 'G1H3BZ', '', NULL, '0', '1', '1', '1', '0', '0', '2021-11-01 06:06:33', '2021-11-06 12:00:47', NULL, '131599157'),
(1346, 'en', 'ChatnGift', 'Jonny', 'jonny.reichert91@gmail.com', 1, 3, '112039512965126125004', 31, '2021-11-02 01:07:41', NULL, 'Wittibsmühle 5, 85368 Wang, Germany', '48.4849786', '11.9236763', '+1', NULL, NULL, 'No Expectation', 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, 1, 3, 1, NULL, 1, '1635835216.', '1347', NULL, NULL, '1347', '1344,1345', NULL, 'English', 2, NULL, NULL, 1, NULL, NULL, 0, NULL, 'V9F03M', '', NULL, '0', '1', '1', '1', '0', '0', '2021-11-02 01:07:41', '2021-11-07 10:52:34', NULL, '131616014'),
(1347, 'en', 'Felix', 'TanteUse', 'colognedog777@gmail.com', 2, 3, '107808874436164149358', 25, '2021-11-05 23:47:17', NULL, 'Technische Universität, 80333 München, Germany', '48.1499083', '11.5665275', '+1', NULL, NULL, 'Relationship, Friendship, Daily Activities, No Expectation', NULL, NULL, 0, NULL, NULL, '2', '3', '4', NULL, 'Yes 5', 1, 1, 2, 3, 1, NULL, 1, '1636219209.', '1346', NULL, NULL, '1346', '1344,1345', NULL, '1', 2, NULL, NULL, 1, NULL, NULL, 0, NULL, 'CPCA9B', '', NULL, '0', '1', '1', '1', '0', '0', '2021-11-06 11:47:17', '2021-11-06 12:08:22', NULL, '131701021');

-- --------------------------------------------------------

--
-- Table structure for table `user_plan_details`
--

CREATE TABLE `user_plan_details` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `plan_id` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` varchar(255) NOT NULL,
  `messages` varchar(255) NOT NULL,
  `sending_requests` varchar(255) NOT NULL,
  `see_love_requests` varchar(255) NOT NULL,
  `recommended_profiles` varchar(255) NOT NULL,
  `recommended_profiles_started_at` datetime NOT NULL,
  `ads` varchar(255) NOT NULL,
  `ads_started_at` datetime NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_plan_details`
--

INSERT INTO `user_plan_details` (`id`, `user_id`, `plan_id`, `type`, `name`, `price`, `messages`, `sending_requests`, `see_love_requests`, `recommended_profiles`, `recommended_profiles_started_at`, `ads`, `ads_started_at`, `created_at`, `updated_at`) VALUES
(135, 1344, '1', 'FREE', 'Free', '0', 'Unlimited', 'Unlimited', 'Unlimited', 'Unlimited', '2021-11-01 11:31:28', 'Unlimited', '2021-11-01 11:31:28', '2021-11-01 06:01:28', '2021-11-01 06:01:28'),
(136, 1345, '1', 'FREE', 'Free', '0', 'Unlimited', 'Unlimited', 'Unlimited', 'Unlimited', '2021-11-01 11:37:33', 'Unlimited', '2021-11-01 11:37:33', '2021-11-01 06:07:33', '2021-11-01 06:07:33'),
(137, 1346, '1', 'FREE', 'Free', '0', 'Unlimited', 'Unlimited', 'Unlimited', 'Unlimited', '2021-11-02 06:40:16', 'Unlimited', '2021-11-02 06:40:16', '2021-11-02 01:10:16', '2021-11-02 01:10:16'),
(138, 1347, '1', 'FREE', 'Free', '0', 'Unlimited', 'Unlimited', 'Unlimited', 'Unlimited', '2021-11-06 17:20:13', 'Unlimited', '2021-11-06 17:20:13', '2021-11-06 11:50:13', '2021-11-06 11:50:13');

-- --------------------------------------------------------

--
-- Table structure for table `user_profile_images`
--

CREATE TABLE `user_profile_images` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `image` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user_profile_images`
--

INSERT INTO `user_profile_images` (`id`, `user_id`, `image`, `created_at`, `updated_at`) VALUES
(1243, 1344, '1635766288.', '2021-11-01 06:01:28', '2021-11-01 06:01:28'),
(1244, 1345, '1635766652.', '2021-11-01 06:07:32', '2021-11-01 06:07:32'),
(1245, 1346, '1635835216.', '2021-11-02 01:10:16', '2021-11-02 01:10:16'),
(1246, 1347, '1636219209.', '2021-11-06 11:50:10', '2021-11-06 11:50:10');

-- --------------------------------------------------------

--
-- Table structure for table `user_tests`
--

CREATE TABLE `user_tests` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_tests`
--

INSERT INTO `user_tests` (`id`, `user_id`, `created_at`, `updated_at`) VALUES
(38, 1347, '2021-11-06 17:36:29', '2021-11-06 17:36:29');

-- --------------------------------------------------------

--
-- Table structure for table `web_users`
--

CREATE TABLE `web_users` (
  `id` int(11) NOT NULL,
  `locale` enum('en','de') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'en' COMMENT 'language en=english, de=germany',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gender` int(11) NOT NULL DEFAULT '0',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `verification_code` int(11) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '1',
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `register_from` enum('web') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `app_links`
--
ALTER TABLE `app_links`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `block_list`
--
ALTER TABLE `block_list`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `chat_requests`
--
ALTER TABLE `chat_requests`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `chat_status`
--
ALTER TABLE `chat_status`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `connect_requests`
--
ALTER TABLE `connect_requests`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `deleted_accounts`
--
ALTER TABLE `deleted_accounts`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `faq`
--
ALTER TABLE `faq`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `intentions`
--
ALTER TABLE `intentions`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `likes`
--
ALTER TABLE `likes`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `matches`
--
ALTER TABLE `matches`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `migrations`
--
ALTER TABLE `migrations`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `notification_descs`
--
ALTER TABLE `notification_descs`
  ADD PRIMARY KEY (`notification_id`,`language_id`) USING BTREE;

--
-- Indexes for table `oauth_access_tokens`
--
ALTER TABLE `oauth_access_tokens`
  ADD PRIMARY KEY (`id`),
  ADD KEY `oauth_access_tokens_user_id_index` (`user_id`);

--
-- Indexes for table `oauth_auth_codes`
--
ALTER TABLE `oauth_auth_codes`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `oauth_clients`
--
ALTER TABLE `oauth_clients`
  ADD PRIMARY KEY (`id`),
  ADD KEY `oauth_clients_user_id_index` (`user_id`);

--
-- Indexes for table `oauth_personal_access_clients`
--
ALTER TABLE `oauth_personal_access_clients`
  ADD PRIMARY KEY (`id`),
  ADD KEY `oauth_personal_access_clients_client_id_index` (`client_id`);

--
-- Indexes for table `oauth_refresh_tokens`
--
ALTER TABLE `oauth_refresh_tokens`
  ADD PRIMARY KEY (`id`),
  ADD KEY `oauth_refresh_tokens_access_token_id_index` (`access_token_id`);

--
-- Indexes for table `password_resets`
--
ALTER TABLE `password_resets`
  ADD KEY `password_resets_email_index` (`email`);

--
-- Indexes for table `paypal_trans`
--
ALTER TABLE `paypal_trans`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `plans`
--
ALTER TABLE `plans`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `posts`
--
ALTER TABLE `posts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `post_ads_user_id_foreign` (`user_id`);

--
-- Indexes for table `post_ads`
--
ALTER TABLE `post_ads`
  ADD PRIMARY KEY (`id`),
  ADD KEY `post_ads_user_id_foreign` (`user_id`);

--
-- Indexes for table `post_likes`
--
ALTER TABLE `post_likes`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `qb_chat_dialog_ids`
--
ALTER TABLE `qb_chat_dialog_ids`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `questions`
--
ALTER TABLE `questions`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `question_descs`
--
ALTER TABLE `question_descs`
  ADD PRIMARY KEY (`question_id`,`language_id`) USING BTREE;

--
-- Indexes for table `ques_options`
--
ALTER TABLE `ques_options`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ques_option_descs`
--
ALTER TABLE `ques_option_descs`
  ADD PRIMARY KEY (`ques_option_id`,`language_id`) USING BTREE;

--
-- Indexes for table `report_chat`
--
ALTER TABLE `report_chat`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `report_images`
--
ALTER TABLE `report_images`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `static_pages`
--
ALTER TABLE `static_pages`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `subscription_plans`
--
ALTER TABLE `subscription_plans`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `support`
--
ALTER TABLE `support`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `test_ques`
--
ALTER TABLE `test_ques`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `test_ques_ans`
--
ALTER TABLE `test_ques_ans`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `test_ques_comparisons`
--
ALTER TABLE `test_ques_comparisons`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `test_scores`
--
ALTER TABLE `test_scores`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_plan_details`
--
ALTER TABLE `user_plan_details`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_profile_images`
--
ALTER TABLE `user_profile_images`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_profile_images_user_id_foreign` (`user_id`);

--
-- Indexes for table `user_tests`
--
ALTER TABLE `user_tests`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `web_users`
--
ALTER TABLE `web_users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admins`
--
ALTER TABLE `admins`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `app_links`
--
ALTER TABLE `app_links`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `block_list`
--
ALTER TABLE `block_list`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `chat_requests`
--
ALTER TABLE `chat_requests`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `chat_status`
--
ALTER TABLE `chat_status`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=265;
--
-- AUTO_INCREMENT for table `connect_requests`
--
ALTER TABLE `connect_requests`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2562;
--
-- AUTO_INCREMENT for table `deleted_accounts`
--
ALTER TABLE `deleted_accounts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `faq`
--
ALTER TABLE `faq`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=67;
--
-- AUTO_INCREMENT for table `intentions`
--
ALTER TABLE `intentions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `likes`
--
ALTER TABLE `likes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1307;
--
-- AUTO_INCREMENT for table `matches`
--
ALTER TABLE `matches`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1152;
--
-- AUTO_INCREMENT for table `migrations`
--
ALTER TABLE `migrations`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;
--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8095;
--
-- AUTO_INCREMENT for table `oauth_clients`
--
ALTER TABLE `oauth_clients`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `oauth_personal_access_clients`
--
ALTER TABLE `oauth_personal_access_clients`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `paypal_trans`
--
ALTER TABLE `paypal_trans`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;
--
-- AUTO_INCREMENT for table `plans`
--
ALTER TABLE `plans`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `posts`
--
ALTER TABLE `posts`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;
--
-- AUTO_INCREMENT for table `post_ads`
--
ALTER TABLE `post_ads`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45;
--
-- AUTO_INCREMENT for table `post_likes`
--
ALTER TABLE `post_likes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;
--
-- AUTO_INCREMENT for table `qb_chat_dialog_ids`
--
ALTER TABLE `qb_chat_dialog_ids`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=134;
--
-- AUTO_INCREMENT for table `questions`
--
ALTER TABLE `questions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;
--
-- AUTO_INCREMENT for table `ques_options`
--
ALTER TABLE `ques_options`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=96;
--
-- AUTO_INCREMENT for table `report_chat`
--
ALTER TABLE `report_chat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;
--
-- AUTO_INCREMENT for table `report_images`
--
ALTER TABLE `report_images`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;
--
-- AUTO_INCREMENT for table `static_pages`
--
ALTER TABLE `static_pages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `subscription_plans`
--
ALTER TABLE `subscription_plans`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `support`
--
ALTER TABLE `support`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `test_ques`
--
ALTER TABLE `test_ques`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=296;
--
-- AUTO_INCREMENT for table `test_ques_ans`
--
ALTER TABLE `test_ques_ans`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=422;
--
-- AUTO_INCREMENT for table `test_ques_comparisons`
--
ALTER TABLE `test_ques_comparisons`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1850;
--
-- AUTO_INCREMENT for table `test_scores`
--
ALTER TABLE `test_scores`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=229;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1348;
--
-- AUTO_INCREMENT for table `user_plan_details`
--
ALTER TABLE `user_plan_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=139;
--
-- AUTO_INCREMENT for table `user_profile_images`
--
ALTER TABLE `user_profile_images`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1247;
--
-- AUTO_INCREMENT for table `user_tests`
--
ALTER TABLE `user_tests`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;
--
-- AUTO_INCREMENT for table `web_users`
--
ALTER TABLE `web_users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=670;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `post_ads`
--
ALTER TABLE `post_ads`
  ADD CONSTRAINT `post_ads_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
