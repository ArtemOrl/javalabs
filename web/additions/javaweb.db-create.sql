use javaweb;

create table `T_FAMILY` (
   `family_id` int not null auto_increment,
   `name` char(200) default null,
   `description` text default null,
   primary key(`family_id`)
) engine=InnoDB default charset=latin1;

create table `T_STYLE` (
   `style_id` int not null auto_increment,
   `family_id` int default null,
   `is_mandatory` tinyint default null,
   `is_multiple` tinyint default null,
   primary key(`style_id`)
) engine=InnoDB default charset=latin1;

create table `T_CLASS` (
   `class_id` int not null auto_increment,
   `name` char(200) default null,
   `description` text default null,
   primary key(`class_id`)
) engine=InnoDB, default charset=latin1;

create table `T_CLASS_STYLE` (
   `class_style_id` int not null auto_increment,
   `class_id` int default null,
   `style_id` int default null,
   primary key(`class_style_id`)
) engine=InnoDB default charset=latin1;

create table `T_OBJECT` (
   `object_id` int not null auto_increment,
   `object_name` char(200) default null,
   `class_id` int default null,
   primary key(`object_id`)
) engine=InnoDB default charset=latin1;

create table `T_OBJECT_VALUE` (
   `object_value_id` int not null auto_increment,
   `object_id` int default null,
   `style_id` int default null,
   `value` char(255) default null,
   primary key(`object_value_id`)
) engine=InnoDB default charset=latin1;

alter table `T_STYLE` add foreign key(family_id) references `T_FAMILY`(family_id);
alter table `T_CLASS_STYLE` add foreign key(class_id) references `T_CLASS`(class_id);
alter table `T_CLASS_STYLE` add foreign key(style_id) references `T_STYLE`(style_id);
alter table `T_OBJECT` add foreign key(class_id) references `T_CLASS`(class_id);
alter table `T_OBJECT_VALUE` add foreign key(object_id) references `T_OBJECT`(object_id);
alter table `T_OBJECT_VALUE` add foreign key(style_id) references `T_STYLE`(style_id);
