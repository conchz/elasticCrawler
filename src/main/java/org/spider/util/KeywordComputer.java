package org.spider.util;

import org.ansj.app.keyword.Keyword;
import org.ansj.dic.LearnTool;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.*;

/**
 * Created on 2015-08-29.
 *
 * @author dolphineor
 */
public class KeywordComputer {

    private int nKeyword = 10;

    // Default constructor keyword number=10
    public KeywordComputer() {
        nKeyword = 10;
    }

    // Constructor set keyword number
    public KeywordComputer(int nKeyword) {
        this.nKeyword = nKeyword;

    }

    // Get keywords object list
    private List<Keyword> computeArticleTfidf(String content, int titleLength) {
        Map<String, Keyword> tm = new HashMap<>();
        LearnTool learn = new LearnTool();
        List<Term> parse = NlpAnalysis.parse(content, learn);
        parse = NlpAnalysis.parse(content, learn);
        for (Term term : parse) {
            int weight = getWeight(term, content.length(), titleLength);
            if (weight == 0)
                continue;
            Keyword keyword = tm.get(term.getName());
            if (keyword == null) {
                keyword = new Keyword(term.getName(), term.termNatures().allFreq, weight);
                tm.put(term.getName(), keyword);
            } else {
                keyword.updateWeight(1);
            }
        }
        TreeSet<Keyword> treeSet = new TreeSet<>(tm.values());
        ArrayList<Keyword> arrayList = new ArrayList<>(treeSet);
        if (treeSet.size() < nKeyword) {
            return arrayList;
        } else {
            return arrayList.subList(0, nKeyword);
        }
    }

    // Get keywords, need title and content
    public Collection<Keyword> computeArticleTfidf(String title, String content) {
        return computeArticleTfidf(title + "\t" + content, title.length());
    }

    // Get keywords, just need content
    public Collection<Keyword> computeArticleTfidf(String content) {
        return computeArticleTfidf(content, 0);
    }

    // Get keywords weight
    private int getWeight(Term term, int length, int titleLength) {
        if (term.getName().matches("(?s)\\d.*")) {
            return 0;
        }
        if (term.getName().trim().length() < 2) {
            return 0;
        }
//        String pos = term.getNatrue().natureStr;
        String pos = term.getNatureStr();
        if (!pos.startsWith("n") || "num".equals(pos)) {
            return 0;
        }
        int weight = 0;
        if (titleLength > term.getOffe()) {
            return 20;
        }
        // position
        double position = (term.getOffe() + 0.0) / length;
        if (position < 0.05)
            return 10;
        weight += (5 - 5 * position);
        return weight;
    }
}
