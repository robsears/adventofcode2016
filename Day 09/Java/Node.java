import java.util.ArrayList;
import java.util.List;

public class Node {

    Long x;
    Long y;
    Long score;

    private String data;
    private List<Node> children;
    private boolean hasChildren;

    Node(Long x, Long y, String data) {
        this.x = x;
        this.y = y;
        this.data = data;
        this.children = getChildren();
        this.hasChildren = (this.children.size() > 0);
        this.score = getScore();
    }

    private List<Node> getChildren() {

        if (this.data.charAt(0) != '(') {
            return new ArrayList<>();
        }

        List<Node> children = new ArrayList<>();
        int p = 0;
        while (p < this.data.length()) {
            if (this.data.charAt(p) == '(') {
                String q = this.data.substring(p, ((p+10 < this.data.length()) ? p + 10 : this.data.length() - 1));
                int e = p+q.indexOf(')');
                String[] sizes = this.data.substring(p+1, e).split("x");
                Long x = Long.parseLong(sizes[0]);
                Long y = Long.parseLong(sizes[1]);
                Node c = new Node(x, y, this.data.substring(e+1, (int) (e+1+x)));
                children.add(c);
                p=(int) (e+1+x);
            }
        }
        return children;
    }

    private Long getScore() {
        Long y = 0L;
        if (this.hasChildren) {
            for (Node c : this.children) {
                y += c.score;
                this.data = this.data.replace(String.format("(%dx%d)%s", c.x, c.y, c.data), "");
            }
            return this.y * y;
        }
        else {
            return this.x * this.y;
        }
    }

}

