package kz.taxikz.controllers.cost;

import kz.taxikz.data.api.pojo.CostData;

public class CostEvents {

    public static class CostFailed {
    }

    public static class CostSuccess {
        CostData costData;

        public CostData getCostData() {
            return this.costData;
        }

        public CostSuccess(CostData costData) {
            this.costData = costData;
        }
    }
}
