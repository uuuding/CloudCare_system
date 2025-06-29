@echo off
echo 测试登录API...

curl -X POST ^
  http://8.137.152.246/:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{
    \"username\": \"1111\",
    \"password\": \"123456\"
}"

echo.
echo 测试完成
pause