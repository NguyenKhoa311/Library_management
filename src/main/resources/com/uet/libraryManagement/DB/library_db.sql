-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 04, 2024 at 12:10 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `library_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `id` int(11) NOT NULL,
  `title` varchar(120) NOT NULL,
  `author` varchar(120) NOT NULL,
  `publisher` varchar(120) NOT NULL,
  `publishDate` varchar(20) NOT NULL,
  `description` text NOT NULL,
  `genre` varchar(50) NOT NULL,
  `thumbnail` text DEFAULT NULL,
  `isbn10` varchar(10) DEFAULT NULL,
  `isbn13` varchar(13) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `createdDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`id`, `title`, `author`, `publisher`, `publishDate`, `description`, `genre`, `thumbnail`, `isbn10`, `isbn13`, `quantity`, `createdDate`) VALUES
(1, 'C++ For Dummies', 'Stephen R. Davis', 'John Wiley & Sons', '2004-04-19', 'If you’ve thought of programmers as elite intelligentsia who possess expertise (and perhaps genes) the rest of us will never have, think again. C++ For Dummies, 5th Edition, debunks the myths, blasts the barriers, shares the secrets, and gets you started. In fact, by the end of Chapter 1, you’ll be able to create a C++ program. OK, it won’t be newest, flashiest video game, but it might be a practical, customized inventory control or record-keeping program. Most people catch on faster when they actually DO something, so C++ For Dummies includes a CD-ROM that gives you all you need to start programming (except the guidance in the book, of course), including: Dev-C, a full-featured, integrated C++ compiler and editor you install to get down to business The source code for the programs in the book, including code for BUDGET, programs that demonstrate principles in the book Documentation for the Standard Template Library Online C++ help files Written by Stephen Randy Davis, author of C++ Weekend Crash Course, C++ for Dummies, takes you through the programming process step-by-step. You’ll discover how to: Generate an executable Create source code, commenting it as you go and using consistent code indentation and naming conventions Write declarations and name variables, and calculate expressions Write and use a function, store sequences in arrays, and declare and use pointer variables Understand classes and object-oriented programming Work with constructors and destructors Use inheritance to extend classes Use stream I/O Comment your code as you go, and use consistent code indentation and naming conventions Automate programming with the Standard Template Library (STL) C++ for Dummies 5th Edition is updated for the newest ANSI standard to make sure you’re up to code. Note: CD-ROM/DVD and other supplementary materials are not included as part of eBook file.', 'Computers', 'http://books.google.com/books/content?id=QuluB-uwHkEC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '0764573942', '9780764573941', 5, '2024-11-23 00:00:00'),
(2, 'C++ GUI Programming with Qt 4', 'Jasmin Blanchette, Mark Summerfield', 'Prentice Hall Professional', '2006', 'Learn GUI programming using Qt4, the powerful crossplatform framework, with the only official Qt book approved by Trolltech.', 'Computers', 'http://books.google.com/books/content?id=tSCR_4LH2KsC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '0131872494', '9780131872493', 5, '2024-11-23 00:00:00'),
(3, 'Symbian OS C++ for Mobile Phones', 'Richard Harrison, Mark Shackman', 'John Wiley & Sons', '2007-06-29', 'Richard Harrison’s existing books are the bestsellers in the Symbian Press Portfolio. His latest book, co-written with Mark Shackman is the successor to \"Symbian OS C++ for Mobile Phones\" Volumes One and Two. Written in the same style as the two previous volumes, this is set to be another gem in the series. The existing material from the volumes will be combined, with explanations and example code updated to reflect the introduction of Symbian OS v9. New and simplified example application will be introduced, which will be used throughout the book. The reference and theory section in particular sets this book apart from the competition and complements other books being proposed at this time. Anyone looking for a thorough insight into Symbian OS C++ before moving onto specialize on particular Symbian OS phones need this book! It will not teach people how to program in C++, but it will reinforce the techniques behind developing applications in Symbian OS C++, and more. This innovative new book covers Symbian OS fundamentals, core concepts and UI. Key highlights include: A quick guide to Kernel Platform security Publishing Applications View Architecture Multi-User games', 'Computers', 'http://books.google.com/books/content?id=61b46Uti8-UC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '047006658X', '9780470066584', 4, '2024-11-23 00:00:00'),
(4, 'C++ Templates', 'David Vandevoorde, Nicolai M. Josuttis', 'Addison-Wesley Professional', '2002-11-12', 'Templates are among the most powerful features of C++, but they are too often neglected, misunderstood, and misused. C++ Templates: The Complete Guide provides software architects and engineers with a clear understanding of why, when, and how to use templates to build and maintain cleaner, faster, and smarter software more efficiently. C++ Templates begins with an insightful tutorial on basic concepts and language features. The remainder of the book serves as a comprehensive reference, focusing first on language details, then on a wide range of coding techniques, and finally on advanced applications for templates. Examples used throughout the book illustrate abstract concepts and demonstrate best practices. Readers learn The exact behaviors of templates How to avoid the pitfalls associated with templates Idioms and techniques, from the basic to the previously undocumented How to reuse source code without threatening performance or safety How to increase the efficiency of C++ programs How to produce more flexible and maintainable software This practical guide shows programmers how to exploit the full power of the template features in C++. The companion Web site at http://www.josuttis.com/tmplbook/ contains sample code and additional updates.', 'Computers', 'http://books.google.com/books/content?id=yQU-NlmQb_UC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '0672334054', '9780672334054', 5, '2024-11-23 00:00:00'),
(5, 'C++ Pocket Reference', 'Kyle Loudon', '\"O\'Reilly Media, Inc.\"', '2008-08-07', 'C++ is a complex language with many subtle facets. This is especially true when it comes to object-oriented and template programming. The C++ Pocket Reference is a memory aid for C++ programmers, enabling them to quickly look up usage and syntax for unfamiliar and infrequently used aspects of the language. The book\'s small size makes it easy to carry about, ensuring that it will always be at-hand when needed. Programmers will also appreciate the book\'s brevity; as much information as possible has been crammed into its small pages.In the C++ Pocket Reference, you will find: Information on C++ types and type conversions Syntax for C++ statements and preprocessor directives Help declaring and defining classes, and managing inheritance Information on declarations, storage classes, arrays, pointers, strings, and expressions Refreshers on key concepts of C++ such as namespaces and scope More! C++ Pocket Reference is useful to Java and C programmers making the transition to C++, or who find themselves occasionally programming in C++. The three languages are often confusingly similar. This book enables programmers familiar with C or Java to quickly come up to speed on how a particular construct or concept is implemented in C++.Together with its companion STL Pocket Reference, the C++ Pocket Reference forms one of the most concise, easily-carried, quick-references to the C++ language available.', 'Computers', 'http://books.google.com/books/content?id=cVckX9HtINwC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '1449378943', '9781449378943', 5, '2024-11-23 00:00:00'),
(6, 'C++ for Finance', 'Robert Johnson', 'HiTeX Press', '2024-10-24', '\"C++ for Finance: Writing Fast and Reliable Trading Algorithms\" serves as an essential guide for both aspiring developers and seasoned finance professionals eager to exploit the power of C++ in trading systems. Addressing the imperative need for speed and precision in financial markets, this book combines comprehensive programming instruction with financial strategies, providing a foundation in C++ that is both technically robust and directly applicable to finance. Each chapter is thoughtfully structured to impart the necessary skills, from understanding financial data structures and advanced C++ concepts, to integrating real-time data feeds and executing sophisticated trading algorithms. With a keen focus on practical application, the book delves into the intricacies of designing, testing, and deploying trading systems. Readers will benefit from detailed discussions on risk management, performance optimization, and automated trade execution, ensuring they are equipped to build systems that are not only innovative but also reliable and secure. Designed to transition seamlessly from basic concepts to advanced strategies, this guide offers the knowledge required to thrive in the dynamic field of algorithmic trading, empowering readers to contribute meaningfully to the future of financial technology.', 'Computers', 'http://books.google.com/books/content?id=0SQsEQAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', 'N/A', 'N/A', 5, '2024-11-23 00:00:00'),
(7, 'Sams Teach Yourself C++ in 24 Hours', 'Jesse Liberty', 'Sams Publishing', '2002', 'Explains core concepts of C++ and how to use it to build object-oriented programs, add rich functionality, debug programs, learn exception and errorhandling techniques, and make code ANSI compliant.', 'Computers', 'http://books.google.com/books/content?id=hklfknZ02eMC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '0672322242', '9780672322242', 5, '2024-11-23 00:00:00'),
(8, 'Vectors, Matrices and C++ Code', 'Sergio Pissanetzky', 'N/A', '2004-10', 'Presented here is an integrated approach - perhaps the first in its class - of the basics of vector and matrix Algebra at College level, with the object-oriented C++ code that implements the vector and matrix objects and brings them to life. Thinking in terms of objects is the natural way of thinking. The concept of object has existed in Science for centuries. More recently, objects were introduced in Computation, and object-oriented programming languages were created. Yet the concept of object is not routinely used when teaching Science, and the idea that objects can come alive in a computer has not yet been fully exploited.This book integrates basic vector and matrix Algebra with object-oriented concepts and the actual code implementing them. It is both a textbook and a software release, complete withsoftware documentation and the mathematical background that supports the code. The source code is included by download and readers can use it for their own programming. The reader will need a basic knowledge of Mathematical notation, Algebra and Trigonometry, but familiarity with C++ is not required because a course on C++ is also included. You should read this book if you are a developer who needs a background in vector or matrix algebra, a science student who needs tolearn C++, a scientist who needs to write advanced code but can\'t waste time developing the basics, or you just need ready-to-use C++ source code for your project.', 'Computers', 'http://books.google.com/books/content?id=-tahz50XWEQC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '0976277506', '9780976277507', 5, '2024-11-23 00:00:00'),
(9, 'Sams Teach Yourself C++ in 21 Days', 'Jesse Liberty, Bradley L. Jones', 'Sams Publishing', '2004-12-14', 'Join the leagues of thousands of programmers and learn C++ from some of the best. The fifth edition of the best seller Sams Teach Yourself C++ in 21 Days, written by Jesse Liberty, a well-known C++ and C# programming manual author and Bradley L. Jones, manager for a number of high profiler developer websites, has been updated to the new ANSI/ISO C++ Standard. This is an excellent hands-on guide for the beginning programmer. Packed with examples of syntax and detailed analysis of code, fundamentals such as managing I/O, loops, arrays and creating C++ applications are all covered in the 21 easy-to-follow lessons. You will also be given access to a website that will provide you will all the source code examples developed in the book as a practice tool. C++ is the preferred language for millions of developers-make Sams Teach Yourself the preferred way to learn it!', 'Computers', 'http://books.google.com/books/content?id=Qs-6bP_4etUC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '076868997X', '9780768689976', 5, '2024-11-23 00:00:00'),
(10, 'Practical C++ Programming', 'Steve Oualline', '\"O\'Reilly Media, Inc.\"', '2003', 'Practical C++ Programming thoroughly covers: C++ syntax · Coding standards and style · Creation and use of object classes · Templates · Debugging and optimization · Use of the C++ preprocessor · File input/output', 'Computers', 'http://books.google.com/books/content?id=pXDzdJ_1E3oC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '0596004192', '9780596004194', 5, '2024-11-23 00:00:00'),
(11, 'Broken Doraemon & Other Poems', 'Manu Kant', 'Book Bazooka Publication', '2021-04-07', 'N/A', 'Poetry', 'http://books.google.com/books/content?id=pIooEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '8195111793', '9788195111794', 5, '2024-11-23 00:00:00'),
(12, 'Japan Pop: Inside the World of Japanese Popular Culture', 'Timothy J. Craig', 'Routledge', '2015-04-08', 'A fascinating illustrated look at various forms of Japanese popular culture: pop song, jazz, enka (a popular ballad genre of music), karaoke, comics, animated cartoons, video games, television dramas, films and \"idols\" -- teenage singers and actors. As pop culture not only entertains but is also a reflection of society, the book is also about Japan itself -- its similarities and differences with the rest of the world, and how Japan is changing. The book features 32 pages of manga plus 50 additional photos, illustrations, and shorter comic samples.', 'Social Science', 'http://books.google.com/books/content?id=ZWXxBwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '1317467205', '9781317467205', 5, '2024-11-23 00:00:00'),
(13, 'The Encyclopedia of Contemporary Japanese Culture', 'Sandra Buckley', 'Taylor & Francis', '2009', 'This encyclopedia covers culture from the end of the Imperialist period in 1945 right up to date to reflect the vibrant nature of contemporary Japanese society and culture.', 'Foreign Language Study', 'http://books.google.com/books/content?id=Wtkm3O3nWXkC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '041548152X', '9780415481526', 5, '2024-11-23 00:00:00'),
(14, 'Manga', 'Shige (CJ) Suzuki, Ronald Stewart', 'Bloomsbury Publishing', '2022-09-22', 'A wide-ranging introductory guide for readers making their first steps into the world of manga, this book helps readers explore the full range of Japanese comic styles, forms and traditions from its earliest texts to the internationally popular comics of the 21st century. In an accessible and easy-to-navigate format, the book covers: · The history of Japanese comics, from influences in early visual culture to the global \'Manga Boom\' of the 1990s to the present · Case studies of texts reflecting the range of themes, genres, forms and creators, including Osamu Tezuka, Machiko Hasegawa and Katsuhiro Otomo · Key themes and contexts – from gender and sexuality, to history and censorship · Critical approaches to manga, including definitions, biography and reception and global publishing contexts The book includes a bibliography of essential critical writing on manga, discussion questions for classroom use and a glossary of key critical terms.', 'Literary Criticism', 'http://books.google.com/books/content?id=3_x9EAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '1350072362', '9781350072367', 5, '2024-11-23 00:00:00'),
(15, 'Network Power', 'Peter J. Katzenstein, Takashi Shiraishi', 'Cornell University Press', '2018-09-05', 'This book examines regional dynamics in contemporary east and southeast Asia, scrutinizing the effects of Japanese dominance on the politics, economics, and cultures of the area. The contributors ask whether Japan has now attained, through sheer economic power and its political and cultural consequences, the predominance it once sought by overtly military means. The discussion is framed by the profound changes of the past decade. Since the end of the Cold War and the breakup of the Soviet Union, regional dynamics increasingly shape international and national developments. This volume places Japan\'s role in Asian regionalism in a broader comparative perspective with European regionalism and the role Germany plays. It assesses the competitive logics of continental and coastal primacy in China. In starkest form, the question addressed is whether Chinese or Japanese domination of the Asian region is more likely. Between a neo-mercantilist emphasis on the world\'s movement toward relatively closed regional blocs and an opposing liberal view that global markets are creating convergent pressures across all national boundaries and regional divides, this book takes a middle position. Asian regionalism is identified by two intersecting developments: Japanese economic penetration of Asian supplier networks through a system of production alliances, and the emergence of a pan-Pacific trading region that includes both Asia and North America. The contributors emphasize factors that are creating an Asia marked by multiple centers of influence, including China and the United States.', 'Political Science', 'http://books.google.com/books/content?id=CFluDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '1501731459', '9781501731457', 5, '2024-11-23 00:00:00'),
(16, 'Doraemon', 'Fujio F. Fujiko', 'N/A', '2019', 'Những câu chuyện về chú mèo máy thông minh Doraemon và nhóm bạn Nobita, Shizuka, Suneo, Jaian, Dekisugi sẽ đưa chúng ta bước vào thế giới hồn nhiên, trong sáng đầy ắp tiếng cười với một kho bảo bối kì diệu - những bảo bối biến ước mơ của chúng ta thành sự thật. Nhưng trên tất cả Doraemon là hiện thân của tình bạn cao đẹp, của niềm khát khao vươn tới những tầm cao.', 'Graphic novels', 'https://i.imgur.com/o0wH4DI.png', '6042128809', '9786042128803', 5, '2024-11-23 00:00:00'),
(17, 'TCP/IP Sockets in Java', 'Kenneth L. Calvert, Michael J. Donahoo', 'Morgan Kaufmann', '2011-08-29', 'The networking capabilities of the Java platform have been extended considerably since the first edition of the book. This new edition covers version 1.5-1.7, the most current iterations, as well as making the following improvements:The API (application programming interface) reference sections in each chapter, which describe the relevant parts of each class, have been replaced with (i) a summary section that lists the classes and methods used in the code, and (ii) a \"gotchas\" section that mentions nonobvious or poorly-documented aspects of the objects. In addition, the book covers several new classes and capabilities introduced in the last few revisions of the Java platform. New abstractions to be covered include NetworkInterface, InterfaceAddress, Inet4/6Address, SocketAddress/InetSocketAddress, Executor, and others; extended access to low-level network information; support for IPv6; more complete access to socket options; and scalable I/O. The example code is also modified to take advantage of new language features such as annotations, enumerations, as well as generics and implicit iterators where appropriate.Most Internet applications use sockets to implement network communication protocols. This book\'s focused, tutorial-based approach helps the reader master the tasks and techniques essential to virtually all client-server projects using sockets in Java. Chapter 1 provides a general overview of networking concepts to allow readers to synchronize the concepts with terminology. Chapter 2 introduces the mechanics of simple clients and servers. Chapter 3 covers basic message construction and parsing. Chapter 4 then deals with techniques used to build more robust clients and servers. Chapter 5 (NEW) introduces the scalable interface facilities which were introduced in Java 1.5, including the buffer and channel abstractions. Chapter 6 discusses the relationship between the programming constructs and the underlying protocol implementations in more detail. Programming concepts are introduced through simple program examples accompanied by line-by-line code commentary that describes the purpose of every part of the program. No other resource presents so concisely or so effectively the material necessary to get up and running with Java sockets programming. Focused, tutorial-based instruction in key sockets programming techniques allows reader to quickly come up to speed on Java applications. Concise and up-to-date coverage of the most recent platform (1.7) for Java applications in networking technology.', 'Computers', 'http://books.google.com/books/content?id=lfHo7uMk7r4C&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '0080568785', '9780080568782', 5, '2024-11-23 00:00:00'),
(18, 'Pokemon', 'Paige V. Polinsky', 'ABDO', '2019-12-15', 'It\'s game on, Pokémon fans! This title explores the inception and evolution of Pokémon, highlighting the game\'s key creators, super players, and the cultural crazes inspired by the game. Special features include side-by-side comparisons of the game over time and a behind-the-screen look into the franchise. Other features include a table of contents, fun facts, a timeline and an index.Full-color photos and action-packed screenshots will transport readers to the heart the Pokémon empire and have everyone excited to learn more! Aligned to Common Core Standards and correlated to state standards. Checkerboard Library is an imprint of Abdo Publishing, a division of ABDO.', 'Juvenile Nonfiction', 'http://books.google.com/books/content?id=GzDDDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '1532178425', '9781532178429', 5, '2024-11-23 00:00:00'),
(19, 'C++ - Visualize IT.', 'N/A', 'Ramraj Raghuvanshi', 'N/A', 'N/A', 'N/A', 'http://books.google.com/books/content?id=PtgrUaPfU0YC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', 'N/A', 'N/A', 5, '2024-11-23 00:00:00'),
(20, 'Naruto', 'Michael Schuerman', 'Michael Schuerman', 'N/A', 'Naruto Uzumaki, the energetic, optimistic, and fearless young ninja of the Leaf Village, woke up to a clear blue sky. The village was vibrant and bustling as always. Naruto had always felt a deep sense of responsibility towards his home. But lately, things were changing. New threats were emerging, and he knew he had to step up to protect those he cared about. He strolled through the village, the bright morning sun casting long shadows on the ground. His friends, fellow ninjas, were also up and about, carrying out their daily duties. He smiled, feeling a sense of camaraderie and belonging. This was his home, these were his people, and he\'d protect them no matter what.', 'Young Adult Fiction', 'http://books.google.com/books/content?id=P_jDEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', 'N/A', 'N/A', 5, '2024-11-23 00:00:00'),
(21, 'The Dragon Ball Z Legend', 'DH Publishing', 'DH Publishing Inc', '2004-05', 'In Akira Toriyama\'s beloved Dragon Ball series, many mysteries remain unsolved and are now explained in this handbook which explores the entire world of Goku.', 'Juvenile Nonfiction', 'http://books.google.com/books/content?id=hEeDFD3KItgC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '0972312498', '9780972312493', 5, '2024-11-23 00:00:00'),
(22, 'Crayon Shinchan', 'Yoshito Usui', 'ComicsOne Corporation', '2005-03-22', 'More misadventures of ShinChan, a bad little five-year-old who manages to cause trouble wherever he goes.', 'Comics & Graphic Novels', 'http://books.google.com/books/content?id=hgZKPgAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api', '1588992713', '9781588992710', 5, '2024-12-01 17:37:13'),
(23, 'Power Rangers', 'Sara Green', 'Bellwether Media', '2018-01-01', 'The Power Rangers are a diverse team! Throughout its history, the team has featured members from a wide variety of backgrounds. Each of the brandÕs many television series and movies boost its message of teamwork and inclusion. Young readers will marvel over this popular brandÕs uprising and continued success!', 'Juvenile Nonfiction', 'http://books.google.com/books/content?id=sLlwDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '1681035154', '9781681035154', 5, '2024-12-01 17:37:34'),
(24, 'Robotics', 'Appuu Kuttan', 'I. K. International Pvt Ltd', '2013-12-30', 'Robotics is an applied engineering science that has been referred to as a combination of machine tool technology and computer science. It includes diverse fields such as machine design, control theory, microelectronics, computer programming, artificial intelligence, human factors and production theory. The present book provides a comprehensive introduction to robotics. The book covers a fair amount of kinematics and dynamics of the robots. It also covers the sensors and actuators used in robotics system. This book will be useful for mechanical, electrical, electronics and computer engineering students. Key Features: Latest technological developments in robotics Robotic classifications, robot programming, robotic sensors and actuators. Kinematics and dynamic analysis of the Robot Modular systems in robotics Advances in Robotics systems Fuzzy logic control in Robotic systems Biped robot Bio-mimetic robot Robot safety and layout Robot calibration Numerical examples Relative merits and demerits of different robot systems', 'Robotics', 'http://books.google.com/books/content?id=5N7NY_YVufkC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '8189866389', '9788189866389', 5, '2024-12-01 17:38:33');

-- --------------------------------------------------------

--
-- Table structure for table `borrow_history`
--

CREATE TABLE `borrow_history` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `doc_type` varchar(10) NOT NULL,
  `doc_id` int(11) NOT NULL,
  `borrow_date` date NOT NULL,
  `due_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  `status` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `borrow_history`
--

INSERT INTO `borrow_history` (`id`, `user_id`, `doc_type`, `doc_id`, `borrow_date`, `due_date`, `return_date`, `status`) VALUES
(1, 1, 'book', 18, '2024-11-21', '2024-12-21', '2024-11-21', 'returned'),
(2, 1, 'thesis', 4, '2024-11-21', '2024-12-21', '2024-11-21', 'returned'),
(3, 1, 'book', 7, '2024-11-22', '2024-12-22', NULL, 'borrowed'),
(4, 1, 'thesis', 16, '2024-11-22', '2024-12-22', NULL, 'borrowed'),
(5, 4, 'book', 6, '2024-11-23', '2024-12-23', NULL, 'borrowed'),
(6, 4, 'thesis', 16, '2024-11-23', '2024-12-23', NULL, 'borrowed'),
(7, 1, 'book', 5, '2024-11-24', '2024-12-08', NULL, 'borrowed'),
(8, 1, 'book', 14, '2024-11-24', '2024-12-08', NULL, 'borrowed'),
(9, 1, 'thesis', 9, '2024-11-24', '2024-12-08', NULL, 'borrowed'),
(10, 4, 'book', 17, '2024-11-24', '2024-12-08', NULL, 'borrowed'),
(11, 4, 'book', 12, '2024-11-24', '2024-12-08', NULL, 'borrowed'),
(12, 4, 'thesis', 13, '2024-11-24', '2024-12-08', NULL, 'borrowed'),
(13, 1, 'book', 3, '2024-11-24', '2024-12-08', NULL, 'borrowed'),
(14, 4, 'book', 21, '2024-11-24', '2024-12-08', '2024-11-29', 'returned'),
(15, 1, 'book', 21, '2024-11-24', '2024-12-08', '2024-11-24', 'returned');

-- --------------------------------------------------------

--
-- Table structure for table `rating_comment`
--

CREATE TABLE `rating_comment` (
  `id` int(11) NOT NULL,
  `doc_id` int(11) NOT NULL,
  `doc_type` varchar(15) NOT NULL,
  `user_id` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  `comment` text NOT NULL,
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rating_comment`
--

INSERT INTO `rating_comment` (`id`, `doc_id`, `doc_type`, `user_id`, `rating`, `comment`, `created_at`) VALUES
(1, 1, 'book', 1, 3, 'hay', '2024-12-04 16:38:35'),
(2, 4, 'book', 1, 5, 'rất hay', '2024-12-04 16:45:16'),
(3, 14, 'book', 4, 3, 'tạm', '2024-12-04 16:54:45');

-- --------------------------------------------------------

--
-- Table structure for table `theses`
--

CREATE TABLE `theses` (
  `id` int(11) NOT NULL,
  `title` varchar(120) NOT NULL,
  `author` varchar(120) NOT NULL,
  `publisher` varchar(120) NOT NULL,
  `publishDate` varchar(20) NOT NULL,
  `description` text NOT NULL,
  `field` varchar(70) NOT NULL,
  `thumbnail` text DEFAULT NULL,
  `isbn10` varchar(10) DEFAULT NULL,
  `isbn13` varchar(13) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `createdDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `theses`
--

INSERT INTO `theses` (`id`, `title`, `author`, `publisher`, `publishDate`, `description`, `field`, `thumbnail`, `isbn10`, `isbn13`, `quantity`, `createdDate`) VALUES
(1, 'Thesis & Dissertation Writing', 'Rivera, Maximiano.M.Jr..', 'Goodwill Trading Co., Inc.', '2007', 'N/A', 'Dissertations, Academic', 'http://books.google.com/books/content?id=ML3HUR6SR30C&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '9715741096', '9789715741095', 5, '2024-11-23 00:00:00'),
(2, 'How to Write Your MBA Thesis', 'Stephanie Jones, Khaled Wahba, Beatrice Van der Heijden', 'Meyer & Meyer Verlag', '2008', 'Written for students of MBA programmes the world over, this guide to writing your thesis covers getting started and planning a schedule, research, the role of the supervisor, writing style, structure, referencing, layout, your defence, marks and publication.', 'Academic writing', 'http://books.google.com/books/content?id=ha9aEkWZZCsC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '1841262315', '9781841262314', 5, '2024-11-23 00:00:00'),
(3, 'How to Write a BA Thesis', 'Charles Lipson', 'University of Chicago Press', '2007-12-01', 'The senior thesis is the capstone of a college education, but writing one can be a daunting prospect. Students need to choose their own topic and select the right adviser. Then they need to work steadily for several months as they research, write, and manage a major independent project. Now there\'s a mentor to help. How to Write a BA Thesis is a practical, friendly guide written by Charles Lipson, an experienced professor who has guided hundreds of students through the thesis-writing process. This book offers step-by-step advice on how to turn a vague idea into a clearly defined proposal, then a draft paper, and, ultimately, a polished thesis. Lipson also tackles issues beyond the classroom-from good work habits to coping with personal problems that interfere with research and writing. Filled with examples and easy-to-use highlighted tips, the book also includes handy time schedules that show when to begin various tasks and how much time to spend on each. Convenient checklists remind students which steps need special attention, and a detailed appendix, filled with examples, shows how to use the three main citation systems in the humanities and social sciences: MLA, APA, and Chicago. How to Write a BA Thesis will help students work more comfortably and effectively-on their own and with their advisers. Its clear guidelines and sensible advice make it the perfect text for thesis workshops. Students and their advisers will refer again and again to this invaluable resource. From choosing a topic to preparing the final paper, How to Write a BA Thesis helps students turn a daunting prospect into a remarkable achievement.', 'Reference', 'http://books.google.com/books/content?id=JBo4Je3qmkcC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '0226481271', '9780226481272', 5, '2024-11-23 00:00:00'),
(4, 'Surviving Your Thesis', 'Suzan Burton, Peter Steane', 'Psychology Press', '2004', 'For those undertaking a higher degree research qualification, \'How To Survive Your Thesis\' describes clearly the challenges and complexities of successfully engaging in both the research process and thesis writing.', 'Business & Economics', 'http://books.google.com/books/content?id=l10OYdv6qDoC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '0415322219', '9780415322218', 5, '2024-11-23 00:00:00'),
(5, 'How To Write Your First Thesis', 'Paul Gruba, Justin Zobel', 'Springer', '2017-08-24', 'Many courses and degrees require that students write a short thesis. This book guides students through their first experience of producing a thesis and undertaking original research. Written by experienced researchers and advisors, the book sets out signposts and tasks to help students to understand what is needed to succeed, including scoping a topic, managing references, interpreting data, and successful completion. For students, the task of writing a thesis is a transition from structured coursework to becoming a researcher. The book provides advice on: What to expect from research and how to work with a supervisor Getting organized and approaching the work in a productive way Developing an overall thesis structure and avoidance of mistakes such as inadvertent plagiarism Producing each major component: a strong introduction, background chapters that are situated in the discipline, and an explanation of methods and results that are crucial to successful original research How to wrap up a complex project with an extended checklist of the many details needed to be checked before a final submission Producing and managing a thesis for the first time can be a daunting task, and this reader-friendly guidebook provides a framework for students to do their best.', 'Computers', 'http://books.google.com/books/content?id=IqYyDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '3319618547', '9783319618548', 5, '2024-11-23 00:00:00'),
(6, 'How to Write the Thesis and Thesis Protocol', 'Piyush Gupta, Dheeraj Shah', 'Jaypee Brothers Medical Publishers', '2020-11-30', 'N/A', 'Medical', 'http://books.google.com/books/content?id=3HkxEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '9390020719', '9789390020713', 5, '2024-11-23 00:00:00'),
(7, 'Church\'s Thesis After 70 Years', 'Adam Olszewski, Jan Wolenski, Robert Janusz', 'Walter de Gruyter', '2013-05-02', 'Church\'s Thesis (CT) was first published by Alonzo Church in 1935. CT is a proposition that identifies two notions: an intuitive notion of a effectively computable function defined in natural numbers with the notion of a recursive function. Despite of the many efforts of prominent scientists, Church\'s Thesis has never been falsified. There exists a vast literature concerning the thesis. The aim of the book is to provide one volume summary of the state of research on Church\'s Thesis. These include the following: different formulations of CT, CT and intuitionism, CT and intensional mathematics, CT and physics, the epistemic status of CT, CT and philosophy of mind, provability of CT and CT and functional programming.', 'Philosophy', 'http://books.google.com/books/content?id=biGH1b6Os5sC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '3110325462', '9783110325461', 5, '2024-11-23 00:00:00'),
(8, 'How to Write a Master′s Thesis', 'Yvonne N. Bui', 'SAGE Publications', '2019-07-31', '\"This is the best textbook about writing an M.A. thesis available in the market.\" –Hsin-I Liu, University of the Incarnate Word The Third Edition of How to Write a Master′s Thesis is a comprehensive manual on how to plan and write a five-chapter master’s thesis, and a great resource for graduate students looking for concrete, applied guidance on how to successfully complete their master′s degrees. While research methods and statistics courses may teach students the basic information on how to conduct research, putting it all together into a single project and document can be a challenge. Author Yvonne Bui demystifies this process by integrating the language learned in prerequisite methods and statistics courses into a step-by-step guide for developing a student′s own thesis or project.', 'Social Science', 'http://books.google.com/books/content?id=JrCcDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '1506336108', '9781506336107', 5, '2024-11-23 00:00:00'),
(9, 'Change and Stability in Thesis and Dissertation Writing', 'Brian Paltridge, Sue Starfield', 'Bloomsbury Publishing', '2023-12-14', 'Examining recent changes in the once stable genre of doctoral thesis and dissertation writing, this book explores how these changes impact on the nature of the doctoral thesis/dissertation itself. Covering different theories of genre, Brian Paltridge and Sue Starfield focus on the concepts of evolution, innovation and emergence in the context of the production and reception of doctoral theses and dissertations. Specifically concerned with this genre in the humanities, social sciences and visual and performing arts, this book also investigates the forces which are shaping changes in this high-stakes genre, as well as those which act as constraints. Employing textography as its methodological approach, the book provides multiple perspectives on the ways in which doctoral theses and dissertations are subject to forces of continuity and change in the academy. Analyses of the \'new humanities\' doctorate, professional doctorates, practice-based doctorates, and the doctorate by publication contribute to understandings of new variants of the doctoral dissertation genre. The book paves the way for a new generation of doctoral students and asks, \'what might the doctorate of the future look like?\'.', 'Language Arts & Disciplines', 'http://books.google.com/books/content?id=SMzlEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '1350146587', '9781350146587', 5, '2024-11-23 00:00:00'),
(11, 'Thesis and Dissertation Writing in a Second Language', 'Brian Paltridge, Sue Starfield', 'Routledge', '2019-07-25', 'Fully updated and packed with new material, the second edition of Thesis and Dissertation Writing in a Second Language is the ideal guide for non-native speaker students and their supervisors working on writing a thesis or dissertation in English. Considering the purposes of thesis and dissertation of writing alongside writer/reader relationships, this book uses accessible language and practical examples to discuss issues that are crucial to successful thesis and dissertation writing. This edition offers: Insights into the experience of being a doctoral writer, issues of writer identity, and writing with authority Typical language and discourse features of theses and dissertations Advice on the structure and organisation of key sections Suggestions for online resources which support writing Extracts from completed theses and dissertations Guidance on understanding examiner expectations Advice on publishing from a PhD Suitable for students from all disciplines, Thesis and Dissertation Writing in a Second Language is essential reading for non-native speaker students looking to complete a thesis or dissertation in English.', 'Education', 'http://books.google.com/books/content?id=Po6lDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '1351690663', '9781351690669', 5, '2024-11-23 00:00:00'),
(12, 'How to Write a Better Thesis', 'David Evans, Paul Gruba, Justin Zobel', 'Springer Science & Business Media', '2014-03-26', 'From proposal to examination, producing a dissertation or thesis is a challenge. Grounded in decades of experience with research training and supervision, this fully updated and revised edition takes an integrated, down-to-earth approach drawing on case studies and examples to guide you step-by-step towards productive success. Early chapters frame the tasks ahead and show you how to get started. From there, practical advice and illustrations take you through the elements of formulating research questions, working with software, and purposeful writing of each of the different kinds of chapters, and finishes with a focus on revision, dissemination and deadlines. How to Write a Better Thesis presents a cohesive approach to research that will help you succeed.', 'Computers', 'http://books.google.com/books/content?id=XwG7BAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '3319042866', '9783319042862', 5, '2024-11-23 00:00:00'),
(13, 'Writing a Watertight Thesis', 'Mike Bottery, Nigel Wright, Mark A. Fabrizi', 'Bloomsbury Publishing', '2023-01-26', 'Writing a doctoral thesis can be an arduous and confusing process. Writing a Watertight Thesis helps you to demystify many doctoral concerns and provides a clear framework for developing a sound structure for your thesis, making your thesis watertight, clear, and defensible. Now with the added experience of Mark A. Fabrizi, the authors draw on their extensive experience of supervising and examining numerous doctorates from an internationally diverse and multicultural student body around the world, including in Australia, Canada, China, Hong Kong, Saudi Arabia, the UK and the USA. The chapters on preparing a research proposal, the viva process, and developing publishable articles out of your thesis have all been updated, and new chapters have been added to demystifying common concerns: Do I have what it takes to do a doctorate? What is doctoral originality? Is my work of doctoral quality? What kind of relationship should I cultivate with my supervisor/advisors? Throughout the book you\'ll find examples showcasing central research questions and the sub-research questions derived from them, descriptions of different ways that doctoral students have achieved success, and exercises that will enable you to apply what you are reading directly to your own thesis.', 'Education', 'http://books.google.com/books/content?id=DEmlEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '1350260622', '9781350260627', 5, '2024-11-23 00:00:00'),
(14, 'Thesis Writing for Master\'s and Ph.D. Program', 'Subhash Chandra Parija, Vikram Kate', 'Springer', '2018-11-03', 'This book on Thesis Writing for Master’s and Ph.D. program focuses on the difficulties students encounter with regard to choosing a guide; selecting an appropriate research title considering the available resources; conducting research; and ways to overcome the hardships they face while researching, writing and preparing their dissertation for submission. Thesis writing is an essential skill that medical and other postgraduates are expected to learn during their academic career as a mandatory partial requirement in order to receive the Master’s degree. However, at the majority of medical schools, writing a thesis is largely based on self-learning, which adds to the burden on students due to the tremendous amount of time spent learning the writing skills in addition to their exhausting clinical and academic work. Due to the difficulties faced during the early grooming years and lack of adequate guidance, acquiring writing skills continues to be a daunting task for most students. This book addresses these difficulties and deficiencies and provides comprehensive guidance, from selecting the research title to publishing in a scientific journal.', 'Medical', 'http://books.google.com/books/content?id=tQ92DwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '981130890X', '9789811308901', 5, '2024-11-23 00:00:00'),
(15, 'Structuring the Thesis', 'David Kember, Michael Corbett', 'Springer', '2018-07-23', 'The book is a collective investigation of the structuring of theses in education, the social sciences and other disciplines that commonly do not follow the standard procedures of the scientific method. To help research students design a structure for their own thesis and liberate their investigations from the constraints associated with the use of the conventional structure, it explains how the structures adopted were designed to suit the topic, methodology and paradigm. It also provides a wide range of examples to draw upon, which suit a broad spectrum of theory, methodological approaches, research methods and paradigms. Additionally, by analyzing the methodologies and paradigms, and reviewing the methodological and paradigmatic spectrum, it offers a significant contribution to the way research is conceptualized. The book addresses a number of key questions faced by students, supervisors and examiners: •Why do examiners often find it difficult to read work in non-scientific disciplines when theses are structured in accordance with the conventional scientific method? •Why do students in non-scientific disciplines struggle to write up the outcomes of their research in the conventional structure? •What alternative thesis structures can be devised to better suit the wide range of methods? •Which theories and paradigms are commonly followed in education and the social sciences and how do these perspectives influence the research process? •What methods, theories and paradigms are commonly adopted by education and social science students and what problems do these pose when students write their theses?', 'Education', 'http://books.google.com/books/content?id=Ly5mDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '9811305110', '9789811305115', 5, '2024-11-23 00:00:00'),
(16, 'Computer Science: A Very Short Introduction', 'Subrata Dasgupta', 'Oxford University Press', '2016-03-24', 'Over the past sixty years, the spectacular growth of the technologies associated with the computer is visible for all to see and experience. Yet, the science underpinning this technology is less visible and little understood outside the professional computer science community. As a scientific discipline, computer science stands alongside the likes of molecular biology and cognitive science as one of the most significant new sciences of the post Second World War era. In this Very Short Introduction, Subrata Dasgupta sheds light on these lesser known areas and considers the conceptual basis of computer science. Discussing algorithms, programming, and sequential and parallel processing, he considers emerging modern ideas such as biological computing and cognitive modelling, challenging the idea of computer science as a science of the artificial. ABOUT THE SERIES: The Very Short Introductions series from Oxford University Press contains hundreds of titles in almost every subject area. These pocket-sized books are the perfect way to get ahead in a new subject quickly. Our expert authors combine facts, analysis, perspective, new ideas, and enthusiasm to make interesting and challenging topics highly readable.', 'Computers', 'http://books.google.com/books/content?id=eM15CwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '0191053201', '9780191053207', 5, '2024-11-23 00:00:00'),
(17, 'Machine Learning', 'GUNRATHI BHARATHKUMAR GOUD', 'Blue Rose Publishers', '2024-01-13', 'Machine learning concepts are going to play important role in providing solutions for applications with usage artificial intelligence. The book provides complete basics and various algorithms involved in solving the problems.', 'Education', 'http://books.google.com/books/content?id=pevtEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', 'N/A', 'N/A', 10, '2024-11-23 00:00:00'),
(18, 'Concrete Solutions', 'Michael Grantham, Carmelo Majorana, Valentina Salomoni', 'CRC Press', '2009-06-10', 'Concrete repair continues to be a subject of major interest to engineers and technologists worldwide. The concrete repair budget for the UK alone currently runs at some UKP 220 per annum. Some estimates have indicated that, worldwide, in 2010 the expenditure for maintenance and repair work will represent about 85% of the total expenditure in the construction field. It has been forecast that, in the same year in the USA, 50 billion dollars will be spent just for the restoration of deteriorated bridges and viaducts. An understanding of the latest techniques in repair and testing and inspection is thus crucial to the international construction industry. This book, with contributions from 34 countries, brings together the best in research, practical application, strategy and theory relating to concrete repair, testing and inspection, fire damage, composites and electro-chemical repair.', 'Technology & Engineering', 'http://books.google.com/books/content?id=-qe1VvnTJlsC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '0415550823', '9780415550826', 5, '2024-12-01 17:39:20'),
(19, 'Probability and Statistics with Applications', 'Leonard A. Asimow, Mark M. Maxwell', 'ACTEX Publications', '2010', 'This text is listed on the Course of Reading for SOA Exam P, and for the CAS Exam ST. Probability and Statistics with Applications: A Problem Solving Text is an introductory textbook designed to make the subject accessible to college freshmen and sophomores concurrent with their study of calculus. The book provides the content to serve as the primary text for a standard two-semester advanced undergraduate course in mathematical probability and statistics. It is organized specifically to meet the needs of students who are preparing for the Society of Actuaries and Casualty Actuarial Society qualifying examination P/1 and the statistics component of CAS Exam 3L. Sample actuarial exam problems are integrated throughout the text along with an abundance of illustrative examples and 799 exercises. The chapters on mathematical statistics cover all of the learning objectives for the statistics portion of the Casualty Actuarial Society Exam ST syllabus. Here again, liberal use is made of past exam problems from CAS Exams 3 and 3L. A separate solutions manual for the text exercises is also available.', 'Actuarial science', 'http://books.google.com/books/content?id=ZL3QQZsp6DAC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '1566987210', '9781566987219', 5, '2024-12-01 17:40:00');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `user_name` varchar(40) NOT NULL,
  `fullName` varchar(50) DEFAULT NULL,
  `password` varchar(40) NOT NULL,
  `birthday` varchar(20) DEFAULT NULL,
  `email` varchar(40) NOT NULL,
  `image` text DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `role` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `user_name`, `fullName`, `password`, `birthday`, `email`, `image`, `phone`, `role`) VALUES
(1, 'user1', 'Khoa Nguyen', '123456', '2005-01-31', 'khoabeo6a1@gmail.com', 'https://i.imgur.com/UrsbMkS.png', '0975847598', 'user'),
(2, 'admin1', 'Khoa', '123456', NULL, 'admin1@gmail.com', 'https://i.imgur.com/1QGmAkh.png', 'N/A', 'admin'),
(3, 'user2', 'KhoaDz', '123456', '2024-11-22', 'khoa_user2@gmail.com', NULL, 'N/A', 'user'),
(4, 'ducdeptrai', NULL, 'quadeptrai', NULL, 'haha@gmail.com', NULL, NULL, 'user'),
(5, 'khoadz', NULL, '123456', NULL, 'khoadz@gmail.com', NULL, NULL, 'user');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `borrow_history`
--
ALTER TABLE `borrow_history`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rating_comment`
--
ALTER TABLE `rating_comment`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `theses`
--
ALTER TABLE `theses`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `borrow_history`
--
ALTER TABLE `borrow_history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `rating_comment`
--
ALTER TABLE `rating_comment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `theses`
--
ALTER TABLE `theses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
