       演示图片:<br>
![test](https://github.com/475789729/SearchableSpinnerDemo/1.png)
![test](https://github.com/475789729/SearchableSpinnerDemo/2.png)
      主要思路是利用系统控件AutoCompleteTextView来充当核心，库代码本身很少很简单，所以保证了健壮性。
<br>
1.用法:
final SearchableSpinner searchableSpinner = findViewById(R.id.searchableSpinner);
        List<String> items = new ArrayList<String>();
        items.add("张飞");
        items.add("关羽");
        items.add("赵云");
        items.add("张翼德");
        items.add("关云长");
        items.add("赵子龙");
        items.add("刘备");
        items.add("刘玄德");
        searchableSpinner.init(items, SearchableSpinner.filterMode_Prefix);

支持SearchableSpinner.filterMode_Prefix和filterMode_Contains，前缀匹配和包含匹配