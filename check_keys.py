import json

# 读取JSON文件
with open('src/main/resources/resources/liquidbounce/lang/zh_cn.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

# 查找所有包含这些关键词的键
keywords = ['clickgui', 'esp', 'hud', 'tnt', 'xray', 'guicloser']

for keyword in keywords:
    print(f'\n包含 "{keyword}" 的键:')
    for key in data.keys():
        if keyword.lower() in key.lower() and '.module.' in key and '.name' in key:
            print(f'  {key}: {data[key]}')