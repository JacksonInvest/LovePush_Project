package com.lovepushapp.model.response;

import java.util.List;

public class GetPersonalityQuestionsResponse {


    /**
     * status : 200
     * questions : {"current_page":1,"data":[{"id":2,"question":"What  is your favourite color ?","is_active":1,"created_at":"2019-01-22 10:18:09","updated_at":"2019-01-22 10:18:09","options":[{"id":5,"personality_question_id":2,"option":"Red","created_at":"2019-01-22 10:18:09","updated_at":"2019-01-22 10:18:09"},{"id":6,"personality_question_id":2,"option":"Orange","created_at":"2019-01-22 10:18:09","updated_at":"2019-01-22 10:18:09"},{"id":7,"personality_question_id":2,"option":"Pink","created_at":"2019-01-22 10:18:09","updated_at":"2019-01-22 10:18:09"},{"id":8,"personality_question_id":2,"option":"Yellow","created_at":"2019-01-22 10:18:09","updated_at":"2019-01-22 10:18:09"}]},{"id":1,"question":"What  is your favourite fruit?","is_active":1,"created_at":"2019-01-21 10:30:40","updated_at":"2019-01-21 10:30:40","options":[{"id":1,"personality_question_id":1,"option":"Mango","created_at":"2019-01-21 10:30:40","updated_at":"2019-01-21 10:30:40"},{"id":2,"personality_question_id":1,"option":"Orange","created_at":"2019-01-21 10:30:41","updated_at":"2019-01-21 10:30:41"},{"id":3,"personality_question_id":1,"option":"Banana","created_at":"2019-01-21 10:30:41","updated_at":"2019-01-21 10:30:41"},{"id":4,"personality_question_id":1,"option":"Apple","created_at":"2019-01-21 10:30:41","updated_at":"2019-01-21 10:30:41"}]}],"first_page_url":"http://14.141.175.109/lovepush/public/api/personality-questions?page=1","from":1,"last_page":1,"last_page_url":"http://14.141.175.109/lovepush/public/api/personality-questions?page=1","next_page_url":null,"path":"http://14.141.175.109/lovepush/public/api/personality-questions","per_page":10,"prev_page_url":null,"to":2,"total":2}
     */

    private int status;
    private QuestionsBean questions;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public QuestionsBean getQuestions() {
        return questions;
    }

    public void setQuestions(QuestionsBean questions) {
        this.questions = questions;
    }

    public static class QuestionsBean {
        /**
         * current_page : 1
         * data : [{"id":2,"question":"What  is your favourite color ?","is_active":1,"created_at":"2019-01-22 10:18:09","updated_at":"2019-01-22 10:18:09","options":[{"id":5,"personality_question_id":2,"option":"Red","created_at":"2019-01-22 10:18:09","updated_at":"2019-01-22 10:18:09"},{"id":6,"personality_question_id":2,"option":"Orange","created_at":"2019-01-22 10:18:09","updated_at":"2019-01-22 10:18:09"},{"id":7,"personality_question_id":2,"option":"Pink","created_at":"2019-01-22 10:18:09","updated_at":"2019-01-22 10:18:09"},{"id":8,"personality_question_id":2,"option":"Yellow","created_at":"2019-01-22 10:18:09","updated_at":"2019-01-22 10:18:09"}]},{"id":1,"question":"What  is your favourite fruit?","is_active":1,"created_at":"2019-01-21 10:30:40","updated_at":"2019-01-21 10:30:40","options":[{"id":1,"personality_question_id":1,"option":"Mango","created_at":"2019-01-21 10:30:40","updated_at":"2019-01-21 10:30:40"},{"id":2,"personality_question_id":1,"option":"Orange","created_at":"2019-01-21 10:30:41","updated_at":"2019-01-21 10:30:41"},{"id":3,"personality_question_id":1,"option":"Banana","created_at":"2019-01-21 10:30:41","updated_at":"2019-01-21 10:30:41"},{"id":4,"personality_question_id":1,"option":"Apple","created_at":"2019-01-21 10:30:41","updated_at":"2019-01-21 10:30:41"}]}]
         * first_page_url : http://14.141.175.109/lovepush/public/api/personality-questions?page=1
         * from : 1
         * last_page : 1
         * last_page_url : http://14.141.175.109/lovepush/public/api/personality-questions?page=1
         * next_page_url : null
         * path : http://14.141.175.109/lovepush/public/api/personality-questions
         * per_page : 10
         * prev_page_url : null
         * to : 2
         * total : 2
         */

        private int current_page;
        private String first_page_url;
        private int from;
        private int last_page;
        private String last_page_url;
        private Object next_page_url;
        private String path;
        private int per_page;
        private Object prev_page_url;
        private int to;
        private int total;
        private List<DataBean> data;

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public String getFirst_page_url() {
            return first_page_url;
        }

        public void setFirst_page_url(String first_page_url) {
            this.first_page_url = first_page_url;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public String getLast_page_url() {
            return last_page_url;
        }

        public void setLast_page_url(String last_page_url) {
            this.last_page_url = last_page_url;
        }

        public Object getNext_page_url() {
            return next_page_url;
        }

        public void setNext_page_url(Object next_page_url) {
            this.next_page_url = next_page_url;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public Object getPrev_page_url() {
            return prev_page_url;
        }

        public void setPrev_page_url(Object prev_page_url) {
            this.prev_page_url = prev_page_url;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 2
             * question : What  is your favourite color ?
             * is_active : 1
             * created_at : 2019-01-22 10:18:09
             * updated_at : 2019-01-22 10:18:09
             * options : [{"id":5,"personality_question_id":2,"option":"Red","created_at":"2019-01-22 10:18:09","updated_at":"2019-01-22 10:18:09"},{"id":6,"personality_question_id":2,"option":"Orange","created_at":"2019-01-22 10:18:09","updated_at":"2019-01-22 10:18:09"},{"id":7,"personality_question_id":2,"option":"Pink","created_at":"2019-01-22 10:18:09","updated_at":"2019-01-22 10:18:09"},{"id":8,"personality_question_id":2,"option":"Yellow","created_at":"2019-01-22 10:18:09","updated_at":"2019-01-22 10:18:09"}]
             */

            private int id;
            private String question;
            private int is_active;
            private String created_at;
            private String updated_at;
            private List<OptionsBean> options;
            private String checkedId;

            public String getCheckedId() {
                return checkedId;
            }

            public void setCheckedId(String checkedId) {
                this.checkedId = checkedId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public int getIs_active() {
                return is_active;
            }

            public void setIs_active(int is_active) {
                this.is_active = is_active;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public List<OptionsBean> getOptions() {
                return options;
            }

            public void setOptions(List<OptionsBean> options) {
                this.options = options;
            }

            public static class OptionsBean {
                /**
                 * id : 5
                 * personality_question_id : 2
                 * option : Red
                 * created_at : 2019-01-22 10:18:09
                 * updated_at : 2019-01-22 10:18:09
                 */

                private int id;
                private int personality_question_id;
                private String option;
                private String created_at;
                private String updated_at;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getPersonality_question_id() {
                    return personality_question_id;
                }

                public void setPersonality_question_id(int personality_question_id) {
                    this.personality_question_id = personality_question_id;
                }

                public String getOption() {
                    return option;
                }

                public void setOption(String option) {
                    this.option = option;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }

                public void setUpdated_at(String updated_at) {
                    this.updated_at = updated_at;
                }
            }
        }
    }
}
