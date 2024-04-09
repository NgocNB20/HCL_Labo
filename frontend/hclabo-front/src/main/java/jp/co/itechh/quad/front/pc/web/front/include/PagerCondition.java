package jp.co.itechh.quad.front.pc.web.front.include;

public interface PagerCondition {
    int NONE_LIMIT = -1;

    int getCount();

    void setCount(int count);

    int getLimit();

    void setLimit(int limit);

    void setOffset(int offset);

    int getOffset();
}
