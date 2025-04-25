-- noinspection SpellCheckingInspectionForFile

-- 初始化用户数据
INSERT INTO app_user (username, password, email, user_type, status, create_time, update_time)
SELECT *
FROM (VALUES ('admin', '{bcrypt}$2a$10$Tz.HPbR9lr8K3mFiUSAyD.K7v1/uO.yvzapJIvsboZDKrAnq8pMrO',
              'admin@astralbridge.space', 1, 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
             ('user1', '{bcrypt}$2a$10$z6pFN/YtkrY5RaOq5XJsteAofLZfq8CQX7gy9Gh5PFjPGvAJrhgt2',
              'xiaoxuan010n@qq.com', 0, 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
             ('vipuser', '{bcrypt}$2a$10$VMXH3gqj3gbwfr8pbQ8zx.q2HPGsP5fwjlUVl9y/hB.lEQ7i8ra42',
              'vip@astralbridge.space', 1, 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()))
         AS temp (username, password, email, user_type, status, create_time, update_time)
WHERE NOT EXISTS (SELECT 1 FROM app_user);

-- 初始化电影类型
INSERT INTO movie_type (name)
SELECT v.name
FROM (VALUES ('动作'), ('传记'), ('犯罪'), ('剧情'), ('爱情')) AS v(name)
WHERE NOT EXISTS (SELECT 1 FROM movie_type);

-- 初始化演员
INSERT INTO actor (name, photo, description)
SELECT v.name, v.photo, v.description
FROM (VALUES ('劳伦斯·哈维', '/api/guest/media/劳伦斯·哈维.jpg',
                 '劳伦斯·哈维是英国著名演员，以其在《罗密欧与朱丽叶》中的出色表演而闻名。他的表演风格细腻且富有层次感，能够精准地捕捉角色的内心世界。哈维在电影和戏剧领域均有卓越成就，是20世纪中期英国影坛的重要人物。'),
                ('苏珊·申塔尔', '/api/guest/media/苏珊·申塔尔.webp',
                 '苏珊·申塔尔是一位才华横溢的女演员，以其在《罗密欧与朱丽叶》中的表演而广受赞誉。她以自然流畅的表演风格和对角色的深刻理解而著称，是那个时代最受欢迎的女演员之一。'),
                ('弗劳拉·罗博森', '/api/guest/media/弗劳拉·罗博森.jpg',
                 '弗劳拉·罗博森是一位英国资深演员，以其在《罗密欧与朱丽叶》中的精彩演出而闻名。她的职业生涯横跨多个年代，凭借其深厚的表演功底和对角色的独特诠释，成为英国戏剧和电影界的传奇人物。'),
                ('沃伦·比蒂', '/api/guest/media/沃伦·比蒂.jpg',
                 '沃伦·比蒂是美国著名演员和导演，以其在《邦妮和克莱德》中的表演而闻名。他不仅是一位出色的演员，还因其在电影制作和导演领域的贡献而备受尊敬。比蒂的作品常常探讨社会问题，具有深刻的思想性。'),
                ('费·唐纳薇', '/api/guest/media/费·唐纳薇.jpg',
                 '费·唐纳薇是美国影坛的传奇人物，以其在《邦妮和克莱德》中的表演而闻名。她以其优雅的气质和强大的表演张力而著称，是20世纪最具影响力的女演员之一。'),
                ('迈克尔·波拉德', '/api/guest/media/迈克尔·波拉德.webp',
                 '迈克尔·波拉德是一位美国演员，以其在《邦妮和克莱德》中的角色而广为人知。他以其独特的表演风格和对角色的深刻理解，成为那个时代最具辨识度的演员之一。')
           ) AS v(name, photo, description)
WHERE NOT EXISTS (SELECT 1 FROM actor);

-- 初始化导演
INSERT INTO director (name, photo, description)
SELECT v.name, v.photo, v.description
FROM (VALUES ('雷纳托·卡斯特拉尼', '/api/guest/media/雷纳托·卡斯特拉尼.webp',
              '雷纳托·卡斯特拉尼是意大利著名的电影导演和编剧，以其对经典文学作品的改编而闻名。他的作品风格细腻，注重人物情感的刻画和社会背景的描绘。卡斯特拉尼最为人熟知的作品是1954年执导的电影《罗密欧与朱丽叶》，这部影片改编自威廉·莎士比亚的同名戏剧，讲述了一段凄美的爱情故事。影片在威尼斯电影节上获得了最高荣誉金狮奖，成为意大利电影史上的经典之作。卡斯特拉尼的导演风格融合了意大利新现实主义的元素，同时注重视觉美感和叙事的戏剧性。他的作品不仅在意大利国内广受好评，也在国际影坛上赢得了广泛的认可。他的电影常常通过细腻的镜头语言和深刻的情感表达，展现人性与社会的复杂关系。'),
             ('阿瑟·佩恩',
              '/api/guest/media/阿瑟·佩恩.jpg',
              '阿瑟·佩恩是美国著名的电影导演和制片人，是新好莱坞浪潮的重要代表人物。他以其大胆的叙事风格和对社会问题的深刻探讨而闻名。佩恩最具代表性的作品是1967年执导的《邦妮和克莱德》，这部影片根据美国历史上著名的雌雄大盗邦妮·派克和克莱德·巴罗的真实经历改编。影片以其创新的叙事手法和对暴力美学的探索，成为新好莱坞电影的开山之作。《邦妮和克莱德》在当时的电影界引起了轰动，不仅获得了奥斯卡金像奖的多项提名，还为佩恩赢得了“后古典好莱坞导演”和“新好莱坞导演”的双重美誉。佩恩的作品常常挑战传统的叙事模式，关注社会边缘人物的命运，展现了美国社会的复杂性和多样性。他的导演风格深刻影响了后来的电影创作者，为新好莱坞浪潮奠定了基础。')
         ) AS v(name, photo, description)
WHERE NOT EXISTS (SELECT 1 FROM director);

-- 初始化电影
INSERT INTO movie (title, description, release_date, duration, cover_image, region, is_vip, play_count, score,
                   create_time, update_time)
SELECT *
FROM (VALUES ('罗密欧与朱丽叶',
              '《罗密欧与朱丽叶》改编自威廉·莎士比亚创作的同名戏剧，影片讲述了一个悲剧的爱情故事。在十五世纪的意大利，两个贵族家族蒙达犹和卡普雷特彼此对立，不断发生血腥争斗。蒙达犹家族中有一个叫做罗密欧的少年，和卡普雷特家族中的小女儿朱丽叶偶遇并相爱。两方家人执意阻挠，在一次争执中罗密欧失手杀死了朱丽叶的表哥，被流放到城外。朱丽叶为了逃避逼婚，在神父的帮助下假死，但获得错误消息的罗密欧以为朱丽叶真的去世，便服毒自杀。醒来后的朱丽叶看到逝去的罗密欧，绝望地用短剑刺入自己的胸膛。本片曾获得1954年第19届威尼斯电影节最高荣誉金狮奖。',
              '1954-11-25（意大利）', '138 分钟（美国） / 142 分钟（东德）',
              'http://cms-bucket.nosdn.127.net/2018/11/21/ff8894800a8849a29f057cb7d511da42.jpeg', '意大利 / 英国', 1, 0,
              9.2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
             ('邦妮和克莱德',
              '《邦妮和克莱德》是由华纳兄弟出品，根据美国历史上著名的雌雄大盗邦妮·派克和克莱德·巴罗的真实经历而拍摄的剧情片。影片讲述了1930年大萧条中，邦妮·派克在得克萨斯州达拉斯市西小镇的母亲家中看到克莱德·巴罗正在偷自己母亲的汽车。克莱德对邦妮一见钟情，向她炫耀自己曾因持械抢劫入狱，并当着她的面抢劫了镇上的小超市。二人从此结伴浪迹天涯，以打劫为生。《邦妮和克莱德》这部由罗伯特·本顿和搭档大卫·纽曼共同编写的剧本，曾被20位导演拒绝，辗转至阿瑟·佩恩才被接下。最终，这部影片如石破天惊般宣告新好莱坞电影浪潮的到来，也为导演赢得了"后古典好莱坞导演"和"新好莱坞导演"的双重美誉。该片在1968年的奥斯卡金像奖中获得9项提名，并最终将最佳女配角和最佳摄影收入囊中。',
              '1967-08-13', '111分钟',
              'http://dingyue.nosdn.127.net/c5L6LFtDREIsPQ4gEhBr9cTdH3EWglNIeV75SoqYnDe7t1542016627595.jpeg', '美国', 0,
              0, 8.8, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP())
         ) AS temp (title, description, release_date, duration, cover_image, region, is_vip, play_count, score,
                    create_time, update_time)
WHERE NOT EXISTS (SELECT 1 FROM movie);

-- 电影类型关联
INSERT INTO movie_movie_type (movie_id, type_id)
SELECT *
FROM (VALUES (1, 4), (1, 5), (2, 1), (2, 2), (2, 3)) AS temp(movie_id, type_id)
WHERE NOT EXISTS (SELECT 1 FROM movie_movie_type);

-- 电影演员关联
INSERT INTO movie_actor (movie_id, actor_id)
SELECT *
FROM (VALUES (1, 1),
             (1, 2),
             (1, 3),
             (2, 4),
             (2, 5)) AS temp(movie_id, actor_id)
WHERE NOT EXISTS (SELECT 1 FROM movie_actor);

-- 电影导演关联
INSERT INTO movie_director (movie_id, director_id)
SELECT *
FROM (VALUES (1, 1), (2, 2)) AS temp(movie_id, director_id)
WHERE NOT EXISTS (SELECT 1 FROM movie_director);