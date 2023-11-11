import com.github.ddd.common.util.JacksonUtil;
import com.github.ddd.common.util.Menu;
import com.github.ddd.common.util.TreeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ranger
 */
public class Main {

    public static void main(String[] args) {
        List<Menu> list = new ArrayList<>();
        list.add(new Menu("1","0","首页",null));
        list.add(new Menu("2","1","系统",null));
        list.add(new Menu("3","1","基础",null));
        list.add(new Menu("2-1","2","用户",null));
        TreeBuilder<Menu, String> builder = new TreeBuilder<>(list);
        Menu tree = builder.buildSingleTree("0", true);
        System.out.println(JacksonUtil.toPrettyJsonStr(tree));
    }
}
