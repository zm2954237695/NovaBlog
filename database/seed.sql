USE novablog;

-- 应用首次启动会自动创建 admin/admin123，并使用 BCrypt 生成密码。
-- 下面的分类和文章可以重复执行，适用于已有 MySQL 数据库初始化。
INSERT INTO blog_category (name, slug, description, sort_order)
SELECT '工程实践', 'engineering', 'Web 工程、架构与效率实践', 1
WHERE NOT EXISTS (SELECT 1 FROM blog_category WHERE slug = 'engineering');

INSERT INTO sys_site_setting (setting_key, setting_value)
VALUES
('site.name', 'NovaBlog'),
('site.description', '用代码记录思考，用实践打磨产品。'),
('site.comment.enabled', 'true')
ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value);
