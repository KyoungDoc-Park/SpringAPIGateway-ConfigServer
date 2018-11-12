
CREATE TABLE route_properties (
	application		VARCHAR(50)	NOT NULL
	, profile		VARCHAR(50)	NOT NULL
	, label			VARCHAR(50)	NOT NULL
	, group_key		VARCHAR(50)	NOT NULL
	, group_type		VARCHAR(50)	NOT NULL
	, group_value		VARCHAR(500)	NOT NULL

	, CONSTRAINT route_properties PRIMARY KEY (group_key, group_type)
)


-- insert sample
INSERT INTO route_properties
	(application, profile, label, group_key, group_type, group_value)
VALUES
	('gateway', 'default', 'master', 'auth', 'path', '/auth/**'),
	('gateway', 'default', 'master', 'auth', 'url', 'http://localhost:8888'),
	('gateway', 'default', 'master', 'auth', 'path', '/cms/**'),
	('gateway', 'default', 'master', 'auth', 'url', 'http://localhost:9999');
