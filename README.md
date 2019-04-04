       演示图片:<br>
![test](https://github.com/475789729/SearchableSpinnerDemo/blob/master/1.png)
![test](https://github.com/475789729/SearchableSpinnerDemo/blob/master/2.png)
主要思路是利用系统控件AutoCompleteTextView来充当核心，库代码本身很少很简单，所以保证了健壮性。
<br>
1.用法:
<br>
final SearchableSpinner searchableSpinner = findViewById(R.id.searchableSpinner);
<br>
        List<String> items = new ArrayList<String>();
<br>
        items.add("张飞");
<br>
        items.add("关羽");
<br>
        items.add("赵云");
<br>
        items.add("张翼德");
<br>
        items.add("关云长");
<br>
        items.add("赵子龙");
<br>
        items.add("刘备");
<br>
        items.add("刘玄德");
<br>
        searchableSpinner.init(items, SearchableSpinner.filterMode_Prefix);
<br>

支持SearchableSpinner.filterMode_Prefix和filterMode_Contains，前缀匹配和包含匹配