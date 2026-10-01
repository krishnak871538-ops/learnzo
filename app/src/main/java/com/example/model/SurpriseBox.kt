package com.example.model

enum class RewardType {
    COINS, STARS, BADGE, STICKER, EXTRA_LIFE, MYSTERY_TICKET
}

data class SurpriseReward(
    val type: RewardType,
    val titleHi: String,
    val titleEn: String,
    val emoji: String,
    val value: Int,
    val descriptionHi: String,
    val descriptionEn: String
) {
    fun getLocalizedTitle(lang: String): String = if (lang == "en") titleEn else titleHi
    fun getLocalizedDescription(lang: String): String = if (lang == "en") descriptionEn else descriptionHi
}

object RewardPool {
    val possibleRewards = listOf(
        SurpriseReward(
            type = RewardType.COINS,
            titleHi = "100 सोने के सिक्के! 🪙",
            titleEn = "100 Golden Coins! 🪙",
            emoji = "🪙",
            value = 100,
            descriptionHi = "अरे वाह! खजाने से 100 चमकदार सोने के सिक्के मिले!",
            descriptionEn = "Hurrah! You discovered 100 shimmering gold coins!"
        ),
        SurpriseReward(
            type = RewardType.COINS,
            titleHi = "50 बोनस सिक्के! 🪙",
            titleEn = "50 Bonus Coins! 🪙",
            emoji = "🪙",
            value = 50,
            descriptionHi = "सुपर! 50 बोनस कॉइन्स आपकी गुल्लक में जुड़े!",
            descriptionEn = "Awesome! 50 bonus coins added to your piggy bank!"
        ),
        SurpriseReward(
            type = RewardType.STARS,
            titleHi = "5 चमचमाते सितारे! ⭐",
            titleEn = "5 Shining Stars! ⭐",
            emoji = "⭐",
            value = 5,
            descriptionHi = "आसमान से 5 जादुई सितारे आपकी टोपी में सज गए!",
            descriptionEn = "5 magical stars landed right into your learner crown!"
        ),
        SurpriseReward(
            type = RewardType.BADGE,
            titleHi = "Maths Wizard Badge 🧙‍♂️",
            titleEn = "Maths Wizard Badge 🧙‍♂️",
            emoji = "🧙‍♂️",
            value = 1,
            descriptionHi = "स्पेशल खिताब! आप बने गणित के जादूगर!",
            descriptionEn = "Special Honor! You unlocked the Math Wizard Badge!"
        ),
        SurpriseReward(
            type = RewardType.BADGE,
            titleHi = "Hindi Ratna Medallion 🏅",
            titleEn = "Hindi Ratna Medallion 🏅",
            emoji = "🏅",
            value = 1,
            descriptionHi = "शानदार सम्मान! हिंदी भाषा रत्न पदक!",
            descriptionEn = "Splendid honor! Hindi Language Ratna Medallion!"
        ),
        SurpriseReward(
            type = RewardType.STICKER,
            titleHi = "सुपर रॉकेट स्टीकर! 🚀",
            titleEn = "Super Rocket Sticker! 🚀",
            emoji = "🚀",
            value = 1,
            descriptionHi = "स्पीड लर्नर का रॉकेट स्टीकर अनलॉक हुआ!",
            descriptionEn = "Speed Learner Rocket Sticker unlocked!"
        ),
        SurpriseReward(
            type = RewardType.EXTRA_LIFE,
            titleHi = "गोल्डन लाइफ हार्ट! ❤️",
            titleEn = "Golden Life Heart! ❤️",
            emoji = "❤️",
            value = 1,
            descriptionHi = "क्विज में गलती होने पर बचाने वाला जादुई दिल!",
            descriptionEn = "A magical life protector heart for your quiz adventures!"
        ),
        SurpriseReward(
            type = RewardType.MYSTERY_TICKET,
            titleHi = "मेगा सरप्राइज गोल्डन टिकट! 🎟️",
            titleEn = "Mega Surprise Golden Ticket! 🎟️",
            emoji = "🎟️",
            value = 1,
            descriptionHi = "अद्भुत! यह गोल्डन टिकट वीकली मेगा प्राइज अनलॉक करेगा!",
            descriptionEn = "Fabulous! This golden ticket qualifies you for the Grand Mega Prize!"
        )
    )

    fun getRandomReward(): SurpriseReward {
        return possibleRewards.random()
    }
}
