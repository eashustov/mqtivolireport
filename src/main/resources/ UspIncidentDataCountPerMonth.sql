SELECT
	"HPC_ASSIGNMENT", to_char("OPEN_TIME", 'Month') AS "MONTH",  to_char("OPEN_TIME", 'MM') AS "MONTH_NUMBER", to_char("OPEN_TIME", 'YYYY') AS "YEAR", COUNT ("NUMBER")AS "COUNT_INC"
FROM
	(	SELECT
			prob1."NUMBER",
			BRIEF_DESCRIPTION,
			PRIORITY_CODE,
			OPEN_TIME,
			to_char(ACTION)                                                           
			AS ACTION,
			REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\s.*){6}.*'), 
			'htt.*$') AS ZABBIX_HISTORY,
		REPLACE
			(
				REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\s.*){4}.*'), 
				'Хост:\s.*$'),
				'Хост: '
			)
			AS HOST,
			REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION),
	'^.*(\s.*){1}.*'),
	'Проблема.*$')                                                              
	AS PROBLEM,
	HPC_STATUS,
	substr(prob1.hpc_assignee_name, 1, instr(prob1.hpc_assignee_name, '(') - 2) 
	AS HPC_ASSIGNEE_NAME,
	substr(prob1.hpc_assignment, 1, instr(prob1.hpc_assignment, '(') - 2 )      
	AS HPC_ASSIGNMENT,
	HPC_CREATED_BY_NAME,
	'RESOLUTION'                                                                
	AS RESOLUTION,
	OPENED_BY 
FROM
	smprimary.probsummarym1 prob1 
WHERE
	prob1.hpc_assignment IN ( 'ЦСИТ Серверы приложений (00001092)',
	'ЦСИТ ОАСП Стандартные платформы (00003984)',
	'Сопровождение WAS ОСЦИТУ ДВБ (00001341)',
	'ЦИ Запад Стандартные платформы (00011217)',
	'ЦИ Центр Стандартные платформы (00011213)',
	'ЦСИТ ОАСП Специализированные платформы (00003978)',
	'ЦСИТ ОАСП Интеграционные платформы (00003982)',
	'ЦИ ОАСП Системы очередей сообщений (00014339)',
	'ЦИ ОАСП Шлюзовые решения (00014345)',
	'ЦИ ОАСП Технологический стэк ППРБ (00014341)',
	'ЦИ Восток Интеграционные платформы (00011221)',
	'ЦИ Запад Интеграционные платформы (00011219)',
	'ЦИ Центр Интеграционные платформы (00011215)',
	'СБТ ДК ОСА Серверы приложений (Щелчков Р.А.) (00010280)',
	'Сопровождение Платформы управления контейнерами (00018435)',
	'SberInfra УСП Интеграционные платформы (Гоголев К.Ю.) (00019273)') 
UNION
SELECT
	prob1."NUMBER",
	BRIEF_DESCRIPTION,
	PRIORITY_CODE,
	OPEN_TIME,
	to_char(ACTION)                                                           AS 
	ACTION,
	REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\s.*){6}.*'), 'htt.*$') AS 
	ZABBIX_HISTORY,
REPLACE
	(
		REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\s.*){4}.*'), 
		'Хост:\s.*$'),
		'Хост: '
	)
	AS HOST,
	REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION),
	'^.*(\s.*){1}.*'),
	'Проблема.*$') AS PROBLEM,
	HPC_STATUS,
	substr(prob1.hpc_assignee_name,
	1,
	instr(prob1.hpc_assignee_name,
	'(') - 2)      AS HPC_ASSIGNEE_NAME,
	substr(prob1.hpc_assignment,
	1,
	instr(prob1.hpc_assignment,
	'(') - 2)      AS HPC_ASSIGNMENT,
	HPC_CREATED_BY_NAME,
	'RESOLUTION'   AS RESOLUTION,
	'OPENED_BY'    AS OPENED_BY 
FROM
	smprimary.SBPROBSUMMARYTSM1 prob1 
WHERE
	prob1.hpc_assignment IN ( 'ЦСИТ Серверы приложений (00001092)',
	'ЦСИТ ОАСП Стандартные платформы (00003984)',
	'Сопровождение WAS ОСЦИТУ ДВБ (00001341)',
	'ЦИ Запад Стандартные платформы (00011217)',
	'ЦИ Центр Стандартные платформы (00011213)',
	'ЦСИТ ОАСП Специализированные платформы (00003978)',
	'ЦСИТ ОАСП Интеграционные платформы (00003982)',
	'ЦИ ОАСП Системы очередей сообщений (00014339)',
	'ЦИ ОАСП Шлюзовые решения (00014345)',
	'ЦИ ОАСП Технологический стэк ППРБ (00014341)',
	'ЦИ Восток Интеграционные платформы (00011221)',
	'ЦИ Запад Интеграционные платформы (00011219)',
	'ЦИ Центр Интеграционные платформы (00011215)',
	'СБТ ДК ОСА Серверы приложений (Щелчков Р.А.) (00010280)',
	'Сопровождение Платформы управления контейнерами (00018435)',
	'SberInfra УСП Интеграционные платформы (Гоголев К.Ю.) (00019273)')) 
WHERE
	OPENED_BY = 'int_zabbix_si' AND OPEN_TIME BETWEEN TO_TIMESTAMP('01.12.2021 00:00:00', 'DD.MM.RRRR HH24:MI:SS') AND TO_TIMESTAMP('17.05.2022 23:59:59', 'DD.MM.RRRR HH24:MI:SS')
GROUP BY "HPC_ASSIGNMENT", to_char("OPEN_TIME", 'Month'), to_char("OPEN_TIME", 'MM'), to_char("OPEN_TIME", 'YYYY')
ORDER BY "HPC_ASSIGNMENT", "YEAR", "MONTH_NUMBER" ASC