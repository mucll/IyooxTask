package com.huisu.iyoox.entity;
import android.text.TextUtils;

import com.huisu.iyoox.Interface.ExercisesType;
import com.huisu.iyoox.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mucll on 2016/9/13.
 */
public class ResQuestionListBean implements Serializable {
    //    SINGLE("单选题", "QT001", 2F, false),
    //    MULTI("多选题", "QT002", 2F, false),
    //    BLANK("填空题", "QT003", 2F, false)
    //    , CHOICE("判断题", "QT004", 2F, false),
    //    RESPONSE("解答题", "QT005", 2F, true),
    //    CALCULATION("计算题", "QT006", 2F, true)
    //    , SUBJECTIVE_BLANK("主观填空题", "QT007", 2F, true);
    public boolean isChecked;   //记录选中状态
    /**
     * 选A比例
     */
    private BigDecimal percent_a;
    /**
     * 选B比例
     */
    private BigDecimal percent_b;
    /**
     * 选C比例
     */
    private BigDecimal percent_c;
    /**
     * 选D比例
     */
    private BigDecimal percent_d;
    /**
     * 选E比例，此项暂时不用，目前选择题只有4个选项
     */
    private BigDecimal percent_e;
    /**
     * 错题本得分率
     */
    private String percent_error;
    /**
     * 学生选择答案
     */
    private String select_answer;
    private int position = -1;//当前选项
    private String analysis;
    private List<String> answer;//正确答案
    private String content;
    private String questionNum;
    private String questionType = ExercisesType.SINGLECHOOSE;
    private String filterType = ExercisesType.SINGLECHOOSE;
    private String uid;
    private String id;
    private List<ChoiceListBean> choiceList;
    private String questionId;
    private String studentAnswer;
    private List<ResQuestionListBean> QuestionList = new ArrayList<>();
    private QustionsAnswers doingAnswers;//学生作的答按对象
    private float studentScore;//学生得分
    private float scoreTotal;//题目的分数
    private String url;
    private long startTime;
    private long endTime;
    private String origin;//习题来源（1--我上传的 2--我收藏的 3--公共题库）
    private List<BlankAnswer> studentBlankAnswerVOs;
    /**
     * markingCount : 0
     * noMarkingCount : 1
     */
    private SubjectiveAttributeBean subjectiveAttribute;
    /**
     * successStudent :
     * errorStudent :
     * successCount : 0
     * errorCount : 0
     */
    private FillAttributeBean fillAttribute;
    /**
     * questionTypeName : null
     * operType : T001
     * chapterIds : ["38106"]
     * knowledgeIds : ["1418"]
     * chapterNames : ["1 柳树醒了"]
     * knowledgeNames : ["小学语文"]
     * answer : ["A"]
     * attachments : null
     * difficulty : 1
     * difficultyName : 容易
     * subjectId : null
     * typeFlag : private
     * useTime : null
     * score : null
     * deleted : 0
     * subQuestions : null
     */
    private String questionTypeName;
    private List<Attach> attachments;
    private int difficulty;
    private String difficultyName;
    private String subjectId;
    private String typeFlag;
    private String useTime;
    private String deleted;
    private String subQuestions;
    private List<String> chapterIds;
    private List<String> knowledgeIds;
    private List<String> chapterNames;
    private List<String> knowledgeNames;
    private String source;
    //目前没有复合题 暂时知识点用String
    private String pointId;
    private String chapterId;

    public String getPercent_error() {
        return percent_error;
    }

    public void setPercent_error(String percent_error) {
        this.percent_error = percent_error;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public float getScoreTotal() {
        if (scoreTotal <= 0) {
            return (int) scoreTotal;
        }
        return scoreTotal;
    }

    public void setScoreTotal(float scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public String getScoreTotalStr() {
        return StringUtils.formatFloat(scoreTotal);
    }

    public double getStudentScore() {
        return studentScore;
    }

    public void setStudentScore(float studentScore) {
        this.studentScore = studentScore;
    }

    public List<BlankAnswer> getStudentBlankAnswerVOs() {
        return studentBlankAnswerVOs;
    }

    public void setStudentBlankAnswerVOs(List<BlankAnswer> studentBlankAnswerVOs) {
        this.studentBlankAnswerVOs = studentBlankAnswerVOs;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public QustionsAnswers getDoingAnswers() {
        return doingAnswers;
    }

    public void setDoingAnswers(QustionsAnswers doingAnswers) {
        this.doingAnswers = doingAnswers;
    }

    public List<ResQuestionListBean> getQuestionList() {
        return QuestionList;
    }

    public void setQuestionList(List<ResQuestionListBean> questionList) {
        QuestionList = questionList;
    }

    public String getId() {
        if (!TextUtils.isEmpty(questionId)) {
            return questionId;
        }
        if (!TextUtils.isEmpty(id))
            return id;
        return uid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    /**
     * img : ["123"]
     * imgUrl : ["123"]
     * option : A
     * value : 违章/$img$/行为
     */
    public String getSelectAnswer() {
        if (position == -1 || StringUtils.isEmpty(choiceList)) {
            return "";
        } else {
            return choiceList.get(position).getOption();
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String replaceImgUrl(String str, List<String> list) {
        if (list == null || list.size() == 0) {
            return str;
        }
        if (str.contains("/$img$/")) {
            String s;
            for (int i = 0; i < list.size(); i++) {
                int indexOf = str.indexOf("/$img$/");
                String ss = str.substring(0, indexOf);
                s = ss + "<img src=" + "\"" + list.get(i) + "\">";
                str = s + str.substring(indexOf + 7, str.length());
            }
        }
        return str;
    }

    public BigDecimal getPercent_a() {
        return percent_a;
    }

    public void setPercent_a(BigDecimal percent_a) {
        this.percent_a = percent_a;
    }

    public BigDecimal getPercent_b() {
        return percent_b;
    }

    public void setPercent_b(BigDecimal percent_b) {
        this.percent_b = percent_b;
    }

    public BigDecimal getPercent_c() {
        return percent_c;
    }

    public void setPercent_c(BigDecimal percent_c) {
        this.percent_c = percent_c;
    }

    public BigDecimal getPercent_d() {
        return percent_d;
    }

    public void setPercent_d(BigDecimal percent_d) {
        this.percent_d = percent_d;
    }

    public BigDecimal getPercent_e() {
        return percent_e;
    }

    public void setPercent_e(BigDecimal percent_e) {
        this.percent_e = percent_e;
    }

    public String getSelect_answer() {
        return select_answer;
    }

    public void setSelect_answer(String select_answer) {
        this.select_answer = select_answer;
    }

    public String getAnalysis() {
        if (TextUtils.isEmpty(analysis)) {
            return "无";
        }
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getAnswer() {
        if (answer != null && answer.size() > 0) {
            String s = "";
            for (int i = 0; i < answer.size(); i++) {
                if (i == answer.size() - 1) {
                    s += answer.get(i);
                } else {
                    s += answer.get(i) + ",";
                }
            }
            return s;
        }
        return "";
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public List<String> getAnswerList() {
        return answer;
    }

    public String getContent() {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(String questionNum) {
        this.questionNum = questionNum;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }


    @Deprecated
    public int getUsedTime() {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        if (endTime == 0) {
            endTime = System.currentTimeMillis();
        }
        if (startTime > endTime) {
            return 0;
        }
        return (int) ((endTime - startTime) / 1000);
    }


    public String getUid() {
        if (!TextUtils.isEmpty(questionId)) {
            return questionId;
        }
        if (!TextUtils.isEmpty(id))
            return id;
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<ChoiceListBean> getChoiceList() {
        return choiceList;
    }

    public void setChoiceList(List<ChoiceListBean> choiceList) {
        this.choiceList = choiceList;
    }

    public String getQuestionId() {
        if (!TextUtils.isEmpty(questionId)) {
            return questionId;
        }
        if (!TextUtils.isEmpty(id))
            return id;
        return uid;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }




    public SubjectiveAttributeBean getSubjectiveAttribute() {
        return subjectiveAttribute;
    }

    public void setSubjectiveAttribute(SubjectiveAttributeBean subjectiveAttribute) {
        this.subjectiveAttribute = subjectiveAttribute;
    }

    public FillAttributeBean getFillAttribute() {
        return fillAttribute;
    }

    public void setFillAttribute(FillAttributeBean fillAttribute) {
        this.fillAttribute = fillAttribute;
    }

    public String getQuestionTypeName() {
        return questionTypeName;
    }

    public void setQuestionTypeName(String questionTypeName) {
        this.questionTypeName = questionTypeName;
    }

    public List<Attach> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attach> attachments) {
        this.attachments = attachments;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getDifficultyName() {
        return difficultyName;
    }

    public void setDifficultyName(String difficultyName) {
        this.difficultyName = difficultyName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTypeFlag() {
        return typeFlag;
    }

    public void setTypeFlag(String typeFlag) {
        this.typeFlag = typeFlag;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getSubQuestions() {
        return subQuestions;
    }

    public void setSubQuestions(String subQuestions) {
        this.subQuestions = subQuestions;
    }

    public List<String> getChapterIds() {
        return chapterIds;
    }

    public void setChapterIds(List<String> chapterIds) {
        this.chapterIds = chapterIds;
    }

    public List<String> getKnowledgeIds() {
        return knowledgeIds;
    }

    public void setKnowledgeIds(List<String> knowledgeIds) {
        this.knowledgeIds = knowledgeIds;
    }

    public List<String> getChapterNames() {
        return chapterNames;
    }

    public void setChapterNames(List<String> chapterNames) {
        this.chapterNames = chapterNames;
    }

    public List<String> getKnowledgeNames() {
        return knowledgeNames;
    }

    public void setKnowledgeNames(List<String> knowledgeNames) {
        this.knowledgeNames = knowledgeNames;
    }

    public String getPointString() {
        if (StringUtils.isEmpty(knowledgeNames)) {
            return "无";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < knowledgeNames.size(); i++) {
            sb.append(knowledgeNames.get(i));
            if (i != knowledgeNames.size() - 1) {
                sb.append("、");
            }
        }
        return sb.toString();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public static class ChoiceListBean implements Serializable {
        private boolean isChecked;
        private String option;
        private String value;
        private int checkedCount;
        private String checkedStudent;
        private List<String> img;

        public void setChecked(boolean isChecked) {
            this.isChecked = isChecked;
        }

        public boolean getIsChecked() {
            return isChecked;
        }

        public String getOption() {
            return option;
        }

        public void setOption(String option) {
            this.option = option;
        }

        public String getValue() {
            if (TextUtils.isEmpty(value)) {
                return "";
            }
            return value.replace("<br/>", "").replaceAll("<span style=\"text-decoration:" +
                    " underline;\">", "<u>").replaceAll("</span>", "</u></span>");
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<String> getImg() {
            return img;
        }

        public void setImg(List<String> img) {
            this.img = img;
        }

        public int getCheckedCount() {
            return checkedCount;
        }

        public void setCheckedCount(int checkedCount) {
            this.checkedCount = checkedCount;
        }

        public String getCheckedStudent() {
            return checkedStudent;
        }

        public void setCheckedStudent(String checkedStudent) {
            this.checkedStudent = checkedStudent;
        }
    }
    public static class SubjectiveAttributeBean implements Serializable {
        private int markingCount;
        private int noMarkingCount;

        public int getMarkingCount() {
            return markingCount;
        }

        public void setMarkingCount(int markingCount) {
            this.markingCount = markingCount;
        }

        public int getNoMarkingCount() {
            return noMarkingCount;
        }

        public void setNoMarkingCount(int noMarkingCount) {
            this.noMarkingCount = noMarkingCount;
        }
    }
    public static class FillAttributeBean implements Serializable {
        private String successStudent;
        private String errorStudent;
        private int successCount;
        private int errorCount;

        public String getSuccessStudent() {
            return successStudent;
        }

        public void setSuccessStudent(String successStudent) {
            this.successStudent = successStudent;
        }

        public String getErrorStudent() {
            return errorStudent;
        }

        public void setErrorStudent(String errorStudent) {
            this.errorStudent = errorStudent;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public void setSuccessCount(int successCount) {
            this.successCount = successCount;
        }

        public int getErrorCount() {
            return errorCount;
        }

        public void setErrorCount(int errorCount) {
            this.errorCount = errorCount;
        }
    }
    public static class BlankAnswer implements Serializable {
        private String singleAnswer;
        private String singleStatus;

        public BlankAnswer(String singleAnswer, String singleStatus) {
            this.singleAnswer = singleAnswer;
            this.singleStatus = singleStatus;
        }

        public BlankAnswer() {
        }

        public String getSingleAnswer() {
            return singleAnswer;
        }

        public void setSingleAnswer(String singleAnswer) {
            this.singleAnswer = singleAnswer;
        }

        public String getSingleStatus() {
            return singleStatus;
        }

        public void setSingleStatus(String singleStatus) {
            this.singleStatus = singleStatus;
        }
    }
    public static class Attach implements Serializable {
        private String url;
        private String type;
        private String fileName;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
