import com.github.ddd.common.pojo.TreeNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ranger
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Menu implements TreeNode<Menu, String> {

    private String id;
    private String pid;
    private String name;
    private List<Menu> children;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String pid() {
        return pid;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Integer weight() {
        return name.hashCode();
    }

    @Override
    public List<Menu> children() {
        return children;
    }

    @Override
    public void fillChildren(List<Menu> children) {
        this.children = children;
    }
}
