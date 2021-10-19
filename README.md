# 在线问卷系统
> 这是个前后端分离的、支持跨域访问的REST风格的`Vue.js`+`Spring Boot`项目，开发工具为`IDEA`，数据库为`MySQL`。由于当时时间匆忙及能力有限，部分功能实现有待进一步优化，可移步参考我最近整理的[SpringBoot 2.x+Maven多模块项目示例](https://github.com/BlueDriver/SpringBoot-rest-demo)进行修改。

》[前端地址](https://github.com/Nice-Ming/Questionnaire-management/tree/master_online)《


》[体验地址](http://www.niceming.cn/questionnaire/)《



# 数据库名称：questionnaire
字符集：UTF-8
## user表
字段         | Type | 长度 |Null| 默认 | 主键 | 唯一 | Explanation
:---        |:--- |:--- |:--- |:--- |:--- |:--- |:--- 
id          |varchar|64| not  |     | Y    |   |用户ID
username    |varchar|64| not  |     |     |   |用户昵称
password    |varchar|64| not  |     |     |   |md5加密后的密码
email       |varchar|64| not  |     |     | Y  |邮箱
create_time |datetime| | not  |     |     |   |用户创建时间
last_login_time|datetime| | null  |null   |     |   |用户最后登录时间
status      |int| | not  | 0    |     |   |用户账号状态<br>0：未激活<br>1：已激活
random_code |varchar|64| not  |     |     |Y   |随机码（用户激活邮箱）

## paper表
字段 | Type | 长度 |Null| 默认 | 主键 | 唯一 | Explanation
:--- |:--- |:--- |:--- |:--- |:--- |:--- |:--- 
id   |varchar|64| not  |     | Y    |   |问卷ID
user_id|varchar|64| not  |     |     |   |用户ID，外键
title|varchar|64| not  |     |     |   |问卷标题
create_time|datetime| | not  |     |     |   |问卷创建时间
status|int| | not  | 0    |     |   |问卷状态<br>0：未发布<br>1：已发布<br>2：已结束<br>3：已删除
start_time|datetime| | null  | null   |     |   |开始时间
end_time|datetime| | null  | null  |     |   |截止时间

## question表
字段 | Type | 长度 |Null| 默认 | 主键 | 唯一 | Explanation
:--- |:--- |:--- |:--- |:--- |:--- |:--- |:--- 
id   |varchar|64| not  |     | Y    |   |问题ID
paper_id|varchar|64| not  |     |     |   |问卷ID，外键
create_time|datetime| | not  |     |     |   |问题创建时间
question_type|int| | not  |    |     |   |问题Type<br>1：单选<br>2：多选<br>3：简答
question_title|varchar|128| not  |     |     |   |问题标题
question_option|varchar|512| not  |     |     |   |问题选项<br>1：选择题，数组字符串<br>[option1,option2,option3...]<br>2：简答题，空数组字符串<br>[]

## answer表
字段 | Type | 长度 |Null| 默认 | 主键 | 唯一 | Explanation
:--- |:--- |:--- |:--- |:--- |:--- |:--- |:--- 
id   |varchar|64| not  |     | Y    |   |答案ID
paper_id|varchar|64| not  |     |     |   |问卷ID，外键
question_id|varchar|64| not  |     |     |   |问题ID，外键
question_type|int| | not  |    |     |   |问题Type<br>1：单选<br>2：多选<br>3：简答
create_time|datetime| | not  |     |     |   |答题时间
answer_option|varchar|512| not  |     |     |   |答题选项<br>1：选择题，来自question表的问题选项，单选题只有一个option，多选至少一个<br>[option1,option2,option3...]<br>2：简答题，至多一个元素的数组字符串<br>["只能有一个元素"]<br>若未达，则无元素<br>[]



# API 

Encoding Method: UTF-8

## 1.Administrator related
### 1.1 Registrar
#### Interface path
```
domain/api/v1/register
```
#### Request Type
HTTP	POST
#### Request Examples
```
{
  "username": "Alice",
  "password": "123456",
  "email": "alice@gmail.com"
}
```
> #### Request Parameters
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---|:---|:---|:---
username|String|Y| 2-64 characters |username
password|String|Y| 6-64 characters |password
email   |String|Y| 5-64个characters and correct format |email

#### Response Parameters
Response Examples
```
{
  "code": 0,
  "msg": "ok",
  "data": 0
}
```
> #### ParametersExplanation
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---  |:---      |:---        |:---
code  |int   |Y         | -          |Request HTTP
msg   |String|Y         | -          |Message
data  |int   |N         | -          |Result Data

#### codeExplanation
Status |Explanation
:---  |:---  
-1    |token failure
0     |request successful
1     |system error
2     |parameter incorrect


#### dataExplanation
Status |Explanation
:---  |:---  
0     |register success
1     |email already in use
2     |email registered but not confirmed


### 1.2 Activation
#### Interface path
```
domain/api/v1/activate
```
#### Request Type
HTTP	GET
Request Examples
```
domain/api/v1/activate/code
```
> #### Request Parameters
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---|:---|:---|:---
code  |String|Y| - |activation


#### Explanation
1. If success, jump to success page
2. Otherwise，jump to invalid page


### 1.3 Log In
#### Interface path
```
domain/api/v1/login
```
#### Request Type
HTTP	POST
#### Request Examples
```
{  
  "email": "alice@gmail.com"，
  "password": "123456"  
}
```
> #### Request Parameters
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---|:---|:---|:---
email   |String|Y| 5-64个字符 |email
password|String|Y| 6-64个字符 |password

#### Response Parameters
Response Examples
```
//successfully logged in
{
  "code": 0,
  "msg": "ok",
  "data": {
    "result": 0,
    "token": "abcdefghijkl12345",
    "username": "Alice",
    "email": "abc@gmail.com
  }
}
//fail
{
  "code": 0,
  "msg": "password error",
  "data": {
    "result": 1
  }
}
```
> #### ParametersExplanation
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---  |:---      |:---        |:---
code  |int   |Y         | -          |HTTP Request
msg   |String|Y         | -          |Message
data  |Object|N         | -          |Result


#### dataExplanation
Parameters    |	Type	  | Required |	Range	| Explanation
:---    |:---    |:---      |:---       |:---
result  |int     |Y         | -         |Result
token   |String  |N          | -        |**Identification after successful login**
username|String  |N          | -        |name
email   |String  |N          | -        |email

#### resultExplanation
取值 |Explanation
:---  |:---  
0     |successfully logged in
1     |password error
2     |user does not exit
3     |account not activated

> header use token example
```
    ...
    headers: {'token': token }
    ...
```


### 1.4 Sign Out
#### Interface path
```
domain/api/v1/admin/logout
```
#### Request Type
HTTP	GET
#### Request Examples
None
> #### Request Parameters
None

#### Response Parameters
Response Examples
```
{
  "code": 0,
  "msg": "ok",
  "data": 0
}
```
> #### ParametersExplanation
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---  |:---      |:---        |:---
code  |int   |Y         | -          |HTTP Request
msg   |String|Y         | -          |Message
data  |int   |N         | -          |Result：0.Logged out，1.Failed



## 2.Survey Related
### 2.1 How des admin access surveys
#### Interface path
```
domain/api/v1/admin/paper-lists
```
#### Request Type
HTTP    GET
#### Request Examples
None
> #### Request Parameters
None
#### Response Parameters
Response Examples
```
//success
{
  "code": 0,
  "msg": "ok",
  "data": [
    {"id": "12345678910","title": "survey", "status": 0, "createTime": 1536887397173, "startTime": "2018-09-20", "endTime": "2018-10-01"},
    {"id": "22345678910","title": "survey title", "status": 1, "createTime": 1536887397666, "startTime": "2018-09-10", "endTime": "2018-10-01"},
    {"id": "32345678910","title": "question", "status": 2, "createTime": 1536887397888, "startTime": "2018-09-10", "endTime": "2018-09-12"},
    {"id": "42345678910","title": "question title", "status": 0, "createTime": 1536887397173, "startTime": "", "endTime": ""}
  ]
}
//fail
{ 
  "code": 1,
  "msg": "server exception"
}
//token expired or failed
{ 
  "code": -1,
  "msg": "token expired or not login",
}
```
> #### ParametersExplanation
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---  |:---      |:---        |:---
id    |String|Y         | -          |survey ID
title |String|Y         | -          |survey title
status|int   |Y         | -          |survey status：0.not published，1.published，2.ended
createTime|long  |Y         | -          |created time stamp
startTime  |String|Y         | -          |survey start time
endTime    |String|Y         | -          |survey end time


### 2.2 Survey Lookup
#### Interface path
```
domain/api/v1/admin/view-paper
```
#### Request Type
HTTP	POST
#### Request Examples
```
{
  "id": "4askfj1093jfi9348oueir932"
}
```
> #### Request Parameters
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---|:---|:---|:---
id    |String|Y         | -         | survey ID

#### Response Parameters
Response Examples
```
{
  "code": 0,
  "msg": "ok",
  "data": {
   "id": "4askfj1093jfi9348oueir932",
   "title": "Introduction Survey",
   "status": 0,
   "createTime": 1536887397173,
   "startTime": "2018-09-12",
   "endTime": "2018-10-01",   
   "questions": [
      {"id": "1234", "questionType":1, "questionTitle": "Blockage/Conjestion of Nose？", "questionOption": ["No Problem", "Mild Problem", "Severe Problem"]},
      {"id": "2234", "questionType":2, "questionTitle": "Runny Nose？", "questionOption": ["Very Mild Problem", "Moderate Problem", "Severe Problem", "As bad as it can be"]},
      {"id": "3234", "questionType":3, "questionTitle": "Smoking History?", "questionOption": []}
    ]
   }
}
```
> #### dataParametersExplanation
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---|:---|:---|:---
id    |String|Y         | -          |survey ID
title |String|Y         | -          |survey title
status|int   |Y         | -          |survey status：0.not published，1.published，2.ended
createTime|long  |Y          | -          |created time stamp
startTime  |String|Y         | -          |survey start time
endTime    |String|Y         | -          |survey end time
questions  |Array |Y         | -          |question list     

> #### questionsParametersExplanation
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---|:---|:---|:---
id    |String|Y         | -          |question ID
questionType  |int   |Y      | -     |question type：1.single choice，2.multiple choice，3.short answer
questionTitle |String|Y      | -     |question title
questionOption|Array |Y      | -     |question options：short answer would be an empty array


### 2.3 Add New Survey
#### Interface path
```
//shared port
domain/api/v1/admin/update-paper
```
#### Request Type
HTTP	POST
#### Request Examples
```
{
  "title": "SNOT-22",
  "startTime": "2018-09-12",
  "endTime": "2018-10-01",
  "status": 0,
  "questions": [
      {"id": "1234", "questionType":1, "questionTitle": "Blockage/Conjestion of Nose？", "questionOption": ["No Problem", "Mild Problem", "Severe Problem"]},
      {"id": "2234", "questionType":2, "questionTitle": "Runny Nose？", "questionOption": ["Very Mild Problem", "Moderate Problem", "Severe Problem", "As bad as it can be"]},
      {"id": "3234", "questionType":3, "questionTitle": "Smoking History?", "questionOption": []}
  ]
}
```
> #### Request Parameters
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---|:---|:---|:---
title       |String|Y| 2-64个字符 | survey name
startTime  |String|Y| 10个字符   | start time
endTime    |String|Y| 10个字符   | end time
status      |int   |Y| 0或1     | survey status，0：not posted；1：posted；

> #### questionsParametersExplanation
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---|:---|:---|:---
questionType  |int   |Y      | -           |question type：1.single choice，2.multiple choice，3.short answer
questionTitle |String|Y      | 1-128 characters   |question title
questionOption|Array |Y      | -     |question options

#### Response Parameters
Response Examples
```
{
  "code": 0,
  "msg": "ok",
  "data": 0
}
```
> #### ParametersExplanation
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---  |:---      |:---        |:---
code  |int   |Y         | -          |-1.token failed，0. success，1.error
msg   |String|Y         | -          |message
data  |int   |N         | -          |0. success

### 2.4 Survey Modification
#### Interface path
```
//share port，with extra Parametersid
domain/api/v1/admin/update-paper
```
#### Request Type
HTTP	POST
#### Request Examples
```
{
   "id": "4askfj1093jfi9348oueir932", 
   "title": "SNOT-22",
   "status": 0,
   "startTime": "2018-09-12",
   "endTime": "2018-10-01",   
   "questions": [
      {"id": "1234", "questionType":1, "questionTitle": "Blockage/Conjestion of Nose？", "questionOption": ["No Problem", "Mild Problem", "Severe Problem"]},
      {"id": "2234", "questionType":2, "questionTitle": "Runny Nose？", "questionOption": ["Very Mild Problem", "Moderate Problem", "Severe Problem", "As bad as it can be"]},
      {"id": "3234", "questionType":3, "questionTitle": "Smoking History?", "questionOption": []}
    ]
   }
}
```
> #### Request Parameters
> #### ParametersExplanation
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---|:---|:---|:---
id    |String|Y         | -          |surtey ID
title |String|Y         | -          |survey title
status|int   |Y         | -          |survey status：0.not posted，1.posted
startTime  |String|Y         | -          |start time
endTime    |String|Y         | -          |end time
questions  |Array |Y         | -          |question list     

> #### questionsParametersExplanation
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---|:---|:---|:---
questionType  |int   |Y      | -     |question time：1.single choice，2.multiple choice，3.short answer
questionTitle |String|Y      | -     |question title
questionOption|Array |Y      | -     |question options

#### Response Parameters
Response Examples
```
{
    "code": 0,
    "msg": "ok",
    "data": 0
}
```
> #### dataParametersExplanation
 Parameters  |	Type	| Required |	Range	| Explanation
 :---  |:---  |:---      |:---        |:---
 data  |int   |Y         | -          |0.success，1.failed，2.survey does not exist



### 2.6 User Lookup（Survey）
#### Interface path
```
domain/api/v1/user/view-paper?id=4askfj1093jfi9348oueir932
```
#### Request Type
HTTP	GET
#### Request Examples
无
> #### Request Parameters
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---|:---|:---|:---
id    |String|Y         | -         | survey id

#### Response Parameters
Response Examples
```
{
  "code": 0,
  "msg": "ok",
  "data": {
   "status": 0,     //only be able to respond when "1"
   "id": "4askfj1093jfi9348oueir932",
   "title": "SNOT-22",   
   "createTime": 1536887397173,
   "startTime": "2018-09-12",
   "endTime": "2018-10-01",
   "questions": [
      {"id": "1234", "questionType":1, "questionTitle": "Blockage/Conjestion of Nose？", "questionOption": ["No Problem", "Mild Problem", "Severe Problem"]},
      {"id": "2234", "questionType":2, "questionTitle": "Runny Nose？", "questionOption": ["Very Mild Problem", "Moderate Problem", "Severe Problem", "As bad as it can be"]},
      {"id": "3234", "questionType":3, "questionTitle": "Smoking History?", "questionOption": []}
    ]
   }
}
```
> #### dataParametersExplanation
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---|:---|:---|:---
status|int   |Y         | -          |survey status：0.not posted，1.posting，2.finished，3.not existed，4.posted but not started
id    |String|N         | -          |survey ID
title |String|N         | -          |survey title
createTime|long  |N          | -          |survey created time
startTime  |String|N         | -          |start time
endTime    |String|N         | -          |end time
questions  |Array |N         | -          |question list     

> #### questionsParametersExplanation
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---|:---|:---|:---
id    |String|Y         | -          |question ID
questionType  |int   |Y      | -     |question type：1.single choice，2.multiple choice，3.short answer
questionTitle |String|Y      | -     |question tile
questionOption|Array |Y      | -     |question choices


### 2.7 提交问卷答案
#### Interface path
```
domain/api/v1/user/commit-paper
```
#### Request Type
HTTP	POST
#### Request Examples
```
//data from view-paper
{
   "id": "4askfj1093jfi9348oueir932",
   "answers": [
      {"id": "1234", "questionType":1,  "answerContent": ["No Problem"]},  //single
      {"id": "2234", "questionType":2,  "answerContent": ["Mild Problem", "Severe Problem"]},  //multiple
      {"id": "3234", "questionType":3,  "answerContent": ["No Smoking History"]}  //short answer
    ]
   }
}
```
> #### Request Parameters
Parameters  |	Type	| Required |	Range	| Explanation
:---  |:---|:---|:---|:---
id    |String|Y         | -          |survey ID
answers  |Array |Y         | -       |answer list  

> #### answersParametersExplanation
Parameters           |Type	  | Required |	Range	| Explanation
:---           |:---  |:---     |:---          |:---
id             |String|Y         | -          |question id
questionType   |int   |Y         | -          |question type：1.single choice，2.multiple choice，3.short answer
answerContent  |-     |Y         | 0-512 characters  |question options

#### Response Parameters
Response Examples
```
{
    "code": 0,
    "msg": "ok"
}
```
> #### dataParametersExplanation
 Parameters  |	Type	| Required |	Range	| Explanation
 :---  |:---  |:---      |:---        |:---
 code  |int   |Y         | -          |0.success，1.fail,2.survey ID invalid

 

 

 


 ### 2.8 查看问卷数据
 #### Interface path
 ```
 domain/api/v1/admin/paper-data
 ```
 #### Request Type
 HTTP	POST
 #### Request Examples
 ```
 {
   "id": "4askfj1093jfi9348oueir932"
 }
 ```
 > #### Request Parameters
 Parameters  |	Type	| Required |	Range	| Explanation
 :---  |:---|:---|:---|:---
 id    |String|Y         | -         | 问卷id

 #### Response Parameters
 Response Examples
 ```
 {
   "code": 0,
   "msg": "ok",
   "data": {
    "id": "4askfj1093jfi9348oueir932",
    "title": "你幸福吗的调查",
    "status": 0,
    "createTime": 1536887397173,
    "startTime": "2018-09-12",
    "endTime": "2018-10-01",   
    "totalCount": 140,
    "questions": [
       {
            "id": "1234", "questionType":1, 
            "questionTitle": "你的收入是多少？", 
            "questionOption": ["2000以下", "2000-5000", "5000+"],
            "answerContent": [10, 30, 100]
       },
       {    
            "id": "2234", "questionType":2, 
            "questionTitle": "你家里有哪些家电？", 
            "questionOption": ["冰箱", "洗衣机", "空调", "麻将机"],
            "answerContent": [30, 40, 80, 20]
       },
       {   
            "id": "3234", "questionType":3, 
            "questionTitle": "说一说你觉得最幸福的事", 
            "questionOption": [],
            "answerContent": [
                "从青铜",
                "到黄金",
                "到王者"              
            ]
       },
       {   
           "id": "4234", "questionType":3, 
           "questionTitle": "说一说你觉得最难过的事", 
           "questionOption": [],
           "answerContent": [
               "从王者",               
               "到青铜" 
           ]
       }
     ]
    }
 }
 ```
 > #### dataParametersExplanation
 Parameters  |	Type	| Required |	Range	| Explanation
 :---  |:---|:---|:---|:---
 id    |String|Y         | -          |问卷ID
 title |String|Y         | -          |问卷标题
 status|int   |Y         | -          |问卷状态：0.未发布，1.已发布，2.已结束
 createTime|long  |Y          | -          |问卷创建时的时间戳
 startTime  |String|Y         | -          |问卷开达日期，若未设置则是空字符串
 endTime    |String|Y         | -          |问卷结束日期，若未设置则是空字符串
 totalCount |int   |Y         | -          |问卷被达总次数（人次）
 questions  |Array |Y         | -          |问题列表     

 > #### questionsParametersExplanation
 Parameters  |	Type	| Required |	Range	| Explanation
 :---  |:---|:---|:---|:---
 id    |String|Y         | -          |问题ID
 questionType  |int   |Y      | -     |问题Type：1.单选题，2.多选题，3.简答题
 questionTitle |String|Y      | -     |问题标题
 questionOption|Array |Y      | -     |问题选项，选择题是Array，简答题为空字符串
 answerContent |Array |Y      | -     |答案内容，选择题中的元素为int，简答题为String

 

 


 ### 2.9 下载问卷模板文件
#### 文件地址
```
domain/template.xls
```
 #### 使用方式
 ```html
 <a href="url">下载模板</a>
 ```


 ### 2.10 上传问卷模板文件生成问卷
 #### Interface path
 ```
 domain/api/v1/admin/upload
 ```
 #### Request Type
 HTTP	POST
 #### Request Examples
 ```
 {
    "file": FILE    //文件
 }
 ```
 > #### Request Parameters
 Parameters  |	Type	| Required |	Range	| Explanation
 :---  |:---|:---|:---|:---
 file  |FILE|Y         | -          |文件模板文件(Excel)

 

 #### Response Parameters
 Response Examples
 ```
 {
     "code": 0,
     "msg": "ok"
 }
 ```
 > #### dataParametersExplanation
  Parameters  |	Type	| Required |	Range	| Explanation
  :---  |:---  |:---      |:---        |:---
  code  |int   |Y         | -          |0.操作成功，1.系统异常，2.文件不合法

  > #### 常见返回值情形
  code  | msg   
  :---  |:---   |
  -1    | 账号未登录或登录已经失效
  0     | ok     
  1     | 异常的具体信息
  2     | 未选择文件！   
  2     | 文件Type不支持！
  2     | 文件大小限制在100KB以内！
  2     | 文件转换失败，请注意格式要求！

  

![image](https://user-images.githubusercontent.com/89793199/137832695-c4699b44-af1a-45bf-9dee-806de73b65c0.png)
