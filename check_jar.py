import json

# 从JAR文件中提取的翻译文件
with open('resources/liquidbounce/lang/zh_cn.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

# 检查用户提到的键
keys = [
    'liquidbounce.module.chilkGUI.name',
    'liquidbounce.module.eSP.name',
    'liquidbounce.module.hUD.name',
    'liquidbounce.module.tNTTimer.name',
    'liquidbounce.module.xRay.name',
    'liquidbounce.module.gUICloser.name'
]

print('JAR文件中的翻译键检查:')
for key in keys:
    if key in data:
        print(f'✅ {key}: {data[key]}')
    else:
        print(f'❌ {key}: 未找到')