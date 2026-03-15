import re
import json

# 读取KillAura相关的所有Kotlin文件
import os

killaura_dir = "src/main/kotlin/net/ccbluex/liquidbounce/features/module/modules/combat/killaura"

# 收集所有设置项名称
settings = []

# 遍历所有文件
for root, dirs, files in os.walk(killaura_dir):
    for file in files:
        if file.endswith('.kt'):
            filepath = os.path.join(root, file)
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()
                # 查找所有设置项
                # 匹配 val xxx by boolean("Name", ...)
                matches = re.findall(r'val\s+\w+\s+by\s+(?:boolean|float|int|floatRange|intRange|enumChoice|multiEnumChoice)\s*\(\s*"([^"]+)"', content)
                settings.extend(matches)

# 去重
settings = list(set(settings))

print("KillAura模块的所有设置项:")
for i, setting in enumerate(sorted(settings), 1):
    print(f"{i}. {setting}")