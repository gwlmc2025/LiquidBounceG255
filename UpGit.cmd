@echo off
git config --global user.email "com.gwl.mc@qq.com"
git config --global user.name "gwlmc2025"
git init
git add .
git commit -m "first commit"
git remote add origin https://github.com/gwlmc2025/LiquidBounceG255.git
git branch -M main
git push -u origin main
pause