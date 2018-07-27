package com.vic.test.applet;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.List;

/**
 * @Author: vic
 * @CreateTime : 2018/7/20 13:18
 */
public class CrazyWord {

    public static void main(String[] args) {
        String body = "{\"Status\": 200, \"Result\": {\"Questions\": [{\"Number\": 1, \"Id\": \"1070\", \"Title\": [{\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u788d\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u9632\"}, {\"Id\": \"\", \"Answer\": \"\\u59a8\"}], \"TimeLong\": 10, \"Index\": 1 }, {\"Number\": 2, \"Id\": \"1423\", \"Title\": [{\"Title\": \"\\u6551\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u7f13\"}, {\"Id\": \"\", \"Answer\": \"\\u63f4\"}], \"TimeLong\": 10, \"Index\": 1 }, {\"Number\": 3, \"Id\": \"1155\", \"Title\": [{\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u5618\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u7a00\"}, {\"Id\": \"\", \"Answer\": \"\\u550f\"}], \"TimeLong\": 10, \"Index\": 1 }, {\"Number\": 4, \"Id\": \"1278\", \"Title\": [{\"Title\": \"\\u8fd0\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u6109\"}, {\"Id\": \"\", \"Answer\": \"\\u8f93\"}], \"TimeLong\": 10, \"Index\": 1 }, {\"Number\": 5, \"Id\": \"1160\", \"Title\": [{\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u522b\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u6808\"}, {\"Id\": \"\", \"Answer\": \"\\u996f\"}], \"TimeLong\": 10, \"Index\": 1 }, {\"Number\": 6, \"Id\": \"1455\", \"Title\": [{\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u53d1\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u8116\"}, {\"Id\": \"\", \"Answer\": \"\\u52c3\"}], \"TimeLong\": 10, \"Index\": 1 }, {\"Number\": 7, \"Id\": \"1068\", \"Title\": [{\"Title\": \"\\u8109\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u640f\"}, {\"Id\": \"\", \"Answer\": \"\\u535a\"}], \"TimeLong\": 10, \"Index\": 0 }, {\"Number\": 8, \"Id\": \"1144\", \"Title\": [{\"Title\": \"\\u6574\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u996c\"}, {\"Id\": \"\", \"Answer\": \"\\u4f24\"}], \"TimeLong\": 10, \"Index\": 0 }, {\"Number\": 9, \"Id\": \"1085\", \"Title\": [{\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u54c1\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u8d5d\"}, {\"Id\": \"\", \"Answer\": \"\\u81ba\"}], \"TimeLong\": 10, \"Index\": 0 }, {\"Number\": 10, \"Id\": \"1282\", \"Title\": [{\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u58f0\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u94c3\"}, {\"Id\": \"\", \"Answer\": \"\\u96f6\"}], \"TimeLong\": 10, \"Index\": 0 }, {\"Number\": 11, \"Id\": \"32\", \"Title\": [{\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u5de5\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u5cfb\"}, {\"Id\": \"\", \"Answer\": \"\\u7ae3\"}], \"TimeLong\": 10, \"Index\": 1 }, {\"Number\": 12, \"Id\": \"252\", \"Title\": [{\"Title\": \"\\u758f\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u5cfb\"}, {\"Id\": \"\", \"Answer\": \"\\u6d5a\"}], \"TimeLong\": 10, \"Index\": 1 }, {\"Number\": 13, \"Id\": \"1073\", \"Title\": [{\"Title\": \"\\u9707\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u61be\"}, {\"Id\": \"\", \"Answer\": \"\\u64bc\"}], \"TimeLong\": 10, \"Index\": 1 }, {\"Number\": 14, \"Id\": \"1438\", \"Title\": [{\"Title\": \"\\u7ed3\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u6784\"}, {\"Id\": \"\", \"Answer\": \"\\u591f\"}], \"TimeLong\": 10, \"Index\": 0 }, {\"Number\": 15, \"Id\": \"1333\", \"Title\": [{\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u5e3d\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u6234\"}, {\"Id\": \"\", \"Answer\": \"\\u5e26\"}], \"TimeLong\": 10, \"Index\": 0 }, {\"Number\": 16, \"Id\": \"511\", \"Title\": [{\"Title\": \"\\u7a77\", \"IsSign\": 0 }, {\"Title\": \"\\u9014\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u8def\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u672b\"}, {\"Id\": \"\", \"Answer\": \"\\u6ca1\"}], \"TimeLong\": 5, \"Index\": 0 }, {\"Number\": 17, \"Id\": \"94\", \"Title\": [{\"Title\": \"\\u6c34\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u4e0d\", \"IsSign\": 0 }, {\"Title\": \"\\u901a\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u6cfb\"}, {\"Id\": \"\", \"Answer\": \"\\u6cc4\"}], \"TimeLong\": 5, \"Index\": 1 }, {\"Number\": 18, \"Id\": \"116\", \"Title\": [{\"Title\": \"\\u9664\", \"IsSign\": 0 }, {\"Title\": \"\\u65e7\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u65b0\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u8865\"}, {\"Id\": \"\", \"Answer\": \"\\u5e03\"}], \"TimeLong\": 5, \"Index\": 1 }, {\"Number\": 19, \"Id\": \"533\", \"Title\": [{\"Title\": \"\\u63d0\", \"IsSign\": 0 }, {\"Title\": \"\\u5fc3\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u80c6\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u6389\"}, {\"Id\": \"\", \"Answer\": \"\\u540a\"}], \"TimeLong\": 5, \"Index\": 1 }, {\"Number\": 20, \"Id\": \"524\", \"Title\": [{\"Title\": \"\\u7d20\", \"IsSign\": 0 }, {\"Title\": \"\\u6627\", \"IsSign\": 0 }, {\"Title\": \"\\u5e73\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u751f\"}, {\"Id\": \"\", \"Answer\": \"\\u8eab\"}], \"TimeLong\": 5, \"Index\": 0 }, {\"Number\": 21, \"Id\": \"304\", \"Title\": [{\"Title\": \"\\u4e0d\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u987e\", \"IsSign\": 0 }, {\"Title\": \"\\u53ca\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u60f6\"}, {\"Id\": \"\", \"Answer\": \"\\u9051\"}], \"TimeLong\": 5, \"Index\": 1 }, {\"Number\": 22, \"Id\": \"708\", \"Title\": [{\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u6ca1\", \"IsSign\": 0 }, {\"Title\": \"\\u65e0\", \"IsSign\": 0 }, {\"Title\": \"\\u95fb\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u6df9\"}, {\"Id\": \"\", \"Answer\": \"\\u6e6e\"}], \"TimeLong\": 5, \"Index\": 1 }, {\"Number\": 23, \"Id\": \"693\", \"Title\": [{\"Title\": \"\\u540a\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u6267\", \"IsSign\": 0 }, {\"Title\": \"\\u7167\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u6d88\"}, {\"Id\": \"\", \"Answer\": \"\\u9500\"}], \"TimeLong\": 5, \"Index\": 1 }, {\"Number\": 24, \"Id\": \"1013\", \"Title\": [{\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u593a\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u4e0f\"}, {\"Id\": \"\", \"Answer\": \"\\u4e10\"}], \"TimeLong\": 5, \"Index\": 0 }, {\"Number\": 25, \"Id\": \"742\", \"Title\": [{\"Title\": \"\\u5b89\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u82e5\", \"IsSign\": 0 }, {\"Title\": \"\\u7d20\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u7f6e\"}, {\"Id\": \"\", \"Answer\": \"\\u4e4b\"}], \"TimeLong\": 5, \"Index\": 1 }, {\"Number\": 26, \"Id\": \"1469\", \"Title\": [{\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u9917\", \"IsSign\": 0 }, {\"Title\": \"\\u4e4b\", \"IsSign\": 0 }, {\"Title\": \"\\u60a3\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u7526\"}, {\"Id\": \"\", \"Answer\": \"\\u9917\"}], \"TimeLong\": 3, \"Index\": 1 }, {\"Number\": 27, \"Id\": \"1044\", \"Title\": [{\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u5e6a\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u5e22\"}, {\"Id\": \"\", \"Answer\": \"\\u5e21\"}], \"TimeLong\": 3, \"Index\": 1 }, {\"Number\": 28, \"Id\": \"221\", \"Title\": [{\"Title\": \"\\u7cbe\", \"IsSign\": 0 }, {\"Title\": \"\\u795e\", \"IsSign\": 0 }, {\"Title\": \"\\u77cd\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u94c4\"}, {\"Id\": \"\", \"Answer\": \"\\u70c1\"}], \"TimeLong\": 3, \"Index\": 0 }, {\"Number\": 29, \"Id\": \"897\", \"Title\": [{\"Title\": \"\\u5b66\", \"IsSign\": 0 }, {\"Title\": \"\\u8bc6\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }, {\"Title\": \"\\u517b\", \"IsSign\": 0 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u4f11\"}, {\"Id\": \"\", \"Answer\": \"\\u4fee\"}], \"TimeLong\": 3, \"Index\": 1 }, {\"Number\": 30, \"Id\": \"971\", \"Title\": [{\"Title\": \"\\u643f\", \"IsSign\": 0 }, {\"Title\": \"?\", \"IsSign\": 1 }], \"Answer\": [{\"Id\": \"\", \"Answer\": \"\\u728b\"}, {\"Id\": \"\", \"Answer\": \"\\u7289\"}], \"TimeLong\": 3, \"Index\": 0 }], \"Revive\": 20 } }";
        execute(body);
    }


    public static void execute(String body) {
        ResponseBody responseBody = JSON.parseObject(body, ResponseBody.class);
        responseBody.getResult().getQuestions().forEach(questions -> {

            String title = questions.getTitle().stream().map(s -> JSON.parseObject(s).getString("Title")).reduce(String::concat).orElse("");
            String answer = questions.getAnswer().stream().map(s -> JSON.parseObject(s).getString("Answer")).reduce((s, s2) -> s + "," + s2).orElse("");

            System.out.println(String.format("第%s题：题目：%s \t 选项：1.%s 2.%s-------\t答案：%s ", questions.getNumber(), title, answer.split(",")[0], answer.split(",")[1], questions.getIndex() + 1));
            if (questions.getNumber() % 5 == 0) {
                System.out.println();
            }
        });
    }

    @Data
    public static class ResponseBody {
        private Integer Status;
        private Result Result;
    }

    @Data
    public static class Result {
        private List<Questions> Questions;
    }

    @Data
    public static class Questions {
        private Integer Number;
        private String Id;
        private List<String> Title;
        private List<String> Answer;
        private Integer Index;
    }

    @Data
    public static class Title {
        private String title;
        private Integer IsSign;
    }

    @Data
    public static class Answer {
        private String Id;
        private String Answer;
    }
}
