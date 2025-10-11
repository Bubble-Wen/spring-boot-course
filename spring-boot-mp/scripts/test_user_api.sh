#!/bin/bash

# API 基础地址，需与服务实际端口匹配
BASE_URL="http://localhost:8888/api/users"

echo "1. 创建用户"
curl -X POST "$BASE_URL" -H "Content-Type: application/json" -d '{
  "username": "user1",
  "password": "123456",
  "nickname": "测试用户001",
  "email": "user1@example.com",
  "phone": "18800000001"
}'
echo -e "\n"

echo "2. 根据ID获取用户"
curl -X GET "$BASE_URL/1"
echo -e "\n"

echo "3. 获取所有用户"
curl -X GET "$BASE_URL"
echo -e "\n"

echo "4. 分页查询用户"
curl -X GET "$BASE_URL/page?current=1&size=5"
echo -e "\n"

echo "5. 根据用户名查询"
curl -X GET "$BASE_URL/username/user1"
echo -e "\n"

echo "6. 根据邮箱查询"
curl -X GET "$BASE_URL/email/user1@example.com"
echo -e "\n"

echo "7. 根据状态查询"
curl -X GET "$BASE_URL/status/1"
echo -e "\n"

echo "8. 模糊查询昵称"
curl -X GET "$BASE_URL/search?nickname=001"
echo -e "\n"

echo "9. 更新用户"
curl -X PUT "$BASE_URL/1" -H "Content-Type: application/json" -d '{
  "username": "user1",
  "password": "123456",
  "nickname": "测试用户1-修改",
  "email": "user1@example.com",
  "phone": "18800000001"
}'
echo -e "\n"

echo "10. 删除用户"
curl -X DELETE "$BASE_URL/1"
echo -e "\n"

echo "11. 批量创建用户"
curl -X POST "$BASE_URL/batch" -H "Content-Type: application/json" -d '[
  {"username": "user2", "password": "123456", "nickname": "测试用户2", "email": "user2@example.com", "phone": "18800000002"},
  {"username": "user3", "password": "123456", "nickname": "测试用户3", "email": "user3@example.com", "phone": "18800000003"}
]'
echo -e "\n"

echo "12. 批量删除用户"
curl -X DELETE "$BASE_URL/batch" -H "Content-Type: application/json" -d '[2,3]'
echo -e "\n"

echo "13. 获取用户总数"
curl -X GET "$BASE_URL/count"
echo -e "\n"

echo "14. 获取活跃用户数"
curl -X GET "$BASE_URL/count/active"
echo -e "\n"

echo "15. 更新用户状态"
curl -X PATCH "$BASE_URL/1/status?status=0"
echo -e "\n"