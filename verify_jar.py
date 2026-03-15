import json

# 从JAR文件中提取的翻译文件
with open('resources/liquidbounce/lang/zh_cn.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

print('JAR文件中的KillAura翻译检查:')
print(f'总翻译条目: {len(data)}')

# 检查一些关键翻译
keys = [
    'liquidbounce.module.killAura.value.Clicker.name',
    'liquidbounce.module.killAura.value.Raycast.name',
    'liquidbounce.module.killAura.value.Criticals.name',
    'liquidbounce.module.killAura.value.KeepSprint.name',
    'liquidbounce.module.killAura.value.BlockMode.name'
]

print('\n关键翻译检查:')
for key in keys:
    if key in data:
        print(f'  ✅ {key}: {data[key]}')
    else:
        print(f'  ❌ {key}: 未找到')