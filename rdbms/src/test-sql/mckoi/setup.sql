create table PP (
   a int not null,
   primary key (a)
);
create table QQ (
   m int not null,
   primary key (m),
   foreign key (m) references PP(a)
);

create table TT (
   a int not null,
   primary key (a)
);
create table UU (
   m int not null,
   n int not null,
   primary key (m,n),
   foreign key (m) references TT(a),
   foreign key (n) references TT(a)
);

create table VV (
   a int not null,
   b int not null,
   primary key (a,b)
);
create table WW (
   m int not null,
   n int not null,
   primary key (m,n),
   foreign key (m,n) references VV(a,b)
);

create table XX (
   a int not null,
   b int not null,
   c int not null,
   d int not null,
   primary key (a,b)
);
create unique index r_a_b_ind on XX(a,b);
create table YY (
   m int not null,
   n int not null,
   o int not null,
   p int not null,
   primary key (m,n),
   foreign key (m,n) references XX(a,b),
   foreign key (o,p) references XX(a,b)
);

create table ZZ (
   a int not null,
   b varchar(32) not null,
   c int not null,
   d int not null,
   primary key (a,b)
);
