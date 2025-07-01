/**
 * @file This file contains classes for encapsulating DeepSeek API request and response data.
 */

// --- Request Classes ---

/**
 * Represents a message in the chat conversation.
 */
class ChatMessage {
  /**
   * @param {string} content The content of the message.
   * @param {string} role The role of the message author (e.g., 'system', 'user', 'assistant').
   */
  constructor(content, role) {
    this.content = content;
    this.role = role;
  }
}

/**
 * Represents the format of the response.
 */
class ResponseFormat {
  /**
   * @param {string} type The type of response format (e.g., 'text').
   */
  constructor(type = 'text') {
    this.type = type;
  }
}

/**
 * Encapsulates the request payload for the DeepSeek Chat API.
 */
class DeepSeekRequest {
  /**
   * @param {object} options The request options.
   * @param {ChatMessage[]} options.messages An array of message objects.
   * @param {string} options.model The model to use for the chat completion.
   */
  constructor({ messages, model }) {
    this.messages = messages;
    this.model = model;
    this.frequency_penalty = 0;
    this.max_tokens = 2048;
    this.presence_penalty = 0;
    this.response_format = new ResponseFormat('text');
    this.stop = null;
    this.stream = false;
    this.stream_options = null;
    this.temperature = 1;
    this.top_p = 1;
    this.tools = null;
    this.tool_choice = 'none';
    this.logprobs = false;
    this.top_logprobs = null;
  }
}

// --- Response Classes ---

/**
 * Represents a choice in the API response.
 */
class ResponseChoice {
  constructor({ index, message, logprobs, finish_reason }) {
    this.index = index;
    this.message = new ChatMessage(message.content, message.role);
    this.logprobs = logprobs;
    this.finish_reason = finish_reason;
  }
}

/**
 * Represents the token usage statistics for the API call.
 */
class UsageStats {
  constructor({ prompt_tokens, completion_tokens, total_tokens, prompt_tokens_details, prompt_cache_hit_tokens, prompt_cache_miss_tokens }) {
    this.prompt_tokens = prompt_tokens;
    this.completion_tokens = completion_tokens;
    this.total_tokens = total_tokens;
    this.prompt_tokens_details = prompt_tokens_details;
    this.prompt_cache_hit_tokens = prompt_cache_hit_tokens;
    this.prompt_cache_miss_tokens = prompt_cache_miss_tokens;
  }
}

/**
 * Encapsulates the response payload from the DeepSeek Chat API.
 */
class DeepSeekResponse {
  constructor({ id, object, created, model, choices, usage, system_fingerprint }) {
    this.id = id;
    this.object = object;
    this.created = created;
    this.model = model;
    this.choices = choices.map(c => new ResponseChoice(c));
    this.usage = new UsageStats(usage);
    this.system_fingerprint = system_fingerprint;
  }
}

export { 
  ChatMessage, 
  ResponseFormat, 
  DeepSeekRequest, 
  ResponseChoice, 
  UsageStats, 
  DeepSeekResponse 
};